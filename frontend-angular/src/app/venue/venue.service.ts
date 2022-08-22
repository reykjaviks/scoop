import { Observable } from "rxjs";
import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { VenueGetDTO } from "../core/data";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})

export class VenueService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  public getVenues(): Observable<VenueGetDTO[]> {
    return this.http.get<VenueGetDTO[]>(`${this.apiServerUrl}/venue/all`)
  }
}
