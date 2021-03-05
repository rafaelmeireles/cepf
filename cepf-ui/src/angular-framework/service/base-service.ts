import {HttpClient} from '@angular/common/http';

import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

import {BaseEntity} from '../model/base-entity';
import {HOST, ROOT_RESOURCE} from '../../app/api';
import {BaseError} from '../error/base-error';

export abstract class BaseService<T extends BaseEntity> {

  private server: string;
  protected url: string;

  protected constructor(private readonly resource: string,
                        protected httpClient: HttpClient) {
    this.server = HOST + ROOT_RESOURCE;
    this.url = this.server + this.resource;
  }

  public findAll(): Observable<T[]> {
    return this.httpClient.get<T[]>(this.url);
  }

  public findById(id: number): Observable<T> {
    return this.httpClient.get<T>(this.url + '/' + id);
  }

  public findOne(entity: T): Observable<T> {
    return this.httpClient.get<T>(this.url + '?entity=' + encodeURI(JSON.stringify(entity))).pipe(
      map( resultList => {
        const values: T[] = resultList as unknown as T[];
        if (values && values.length > 1) {
          throw new BaseError(500, 'A operação executada retornou mais de um registro');
        }
        return resultList[0];
      })
    );
  }

  public find(entity: T): Observable<T[]> {
    // return this.httpClient.get<T[]>(this.url, {params: JSON.parse(JSON.stringify(entity))});
    // return this.httpClient.get<T[]>(this.url + '?entity=' + JSON.stringify(entity));
    return this.httpClient.get<T[]>(this.url + '?entity=' + encodeURI(JSON.stringify(entity)));
  }

  public persist(entity: T): Observable<T> {
    return this.httpClient.post<T>(this.url, entity);
  }

  public update(entity: T): Observable<T> {
    return this.httpClient.put<T>(this.url, entity);
  }

  public delete(id: number): Observable<T> {
    return this.httpClient.delete<T>(this.url + '/' + id);
  }

  // public delete(entity: T): Observable<T> {
  //   return this.httpClient.delete<T>(this.url, {params: JSON.parse(JSON.stringify(entity))});
  // }
}
