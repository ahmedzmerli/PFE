import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Pdv } from '../models/pdv.model';
import { LinkMsisdnsRequest} from "../models/LinkMsisdnsRequest.model";
import {PdvHistory} from "../models/pdvhistory.model";

@Injectable({
  providedIn: 'root'
})
export class PdvService {
  private apiUrl = 'http://localhost:8081/api/v1/pdv';

  constructor(private http: HttpClient) {}

  /**
   * Créer un nouveau PDV avec un msisdn
   */
  create(pdv: Pdv): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}`, pdv);
  }

  /**
   * Supprimer un msisdn complètement
   */
  delete(msisdn: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${msisdn}`);
  }

  /**
   * Récupérer la liste de tous les PDVs + msisdns
   */
  listAll(): Observable<Pdv[]> {
    return this.http.get<Pdv[]>(`${this.apiUrl}`);
  }

  /**
   * Ajouter un msisdn à un PDV existant
   */
  addMsisdn(idPdvMaster: number, msisdn: string): Observable<void> {
    return this.http.post<void>(
      `${this.apiUrl}/${idPdvMaster}/add-msisdn`,
      {},
      { params: { msisdn } }
    );
  }

  /**
   * Supprimer un msisdn d'un PDV existant
   */
  removeMsisdn(idPdvMaster: number, msisdn: string): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${idPdvMaster}/remove-msisdn`,
      { params: { msisdn } }
    );
  }

  /**
   * Lister les msisdns d'un PDV
   */
  listMsisdns(idPdvMaster: number): Observable<string[]> {
    return this.http.get<string[]>(
      `${this.apiUrl}/${idPdvMaster}/msisdns`
    );
  }

  /**
   * Mettre à jour les infos d'un PDV
   */
  update(idPdvMaster: number, pdv: Partial<Pdv>): Observable<void> {
    return this.http.put<void>(
      `${this.apiUrl}/${idPdvMaster}`,
      pdv
    );
  }

  /**
   * Lier plusieurs msisdns existants à un nouveau PDV
   */
  linkExistingMsisdns(request: LinkMsisdnsRequest): Observable<void> {
    return this.http.post<void>(
      `${this.apiUrl}/link-msisdns`,
      request
    );
  }


  getHistory(): Observable<PdvHistory[]> {
    return this.http.get<PdvHistory[]>('http://localhost:8081/api/v1/pdv-history');
  }

}
