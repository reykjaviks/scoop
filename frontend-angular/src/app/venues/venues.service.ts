import { Observable } from "rxjs";
import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment";
import { Venue } from "./interfaces/venue";
import { NeighbourhoodInfo } from "./interfaces/neighbourhood-info";

@Injectable({
  providedIn: 'root'
})
export class VenuesService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  public getVenues(): Observable<Venue[]> {
    return this.http.get<Venue[]>(`${this.apiServerUrl}/venue/all`)
  }

  public getNeighbourhoodInfos(): Observable<NeighbourhoodInfo[]> {
    return this.http.get<NeighbourhoodInfo[]>(`${this.apiServerUrl}/neighbourhood/venue-count`)
  }
}
