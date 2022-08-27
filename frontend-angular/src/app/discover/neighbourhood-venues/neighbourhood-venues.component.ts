import { Component, OnInit } from '@angular/core';
import { NeighbourhoodVenuesService } from './neighbourhood-venues.service';
import { HttpErrorResponse } from "@angular/common/http";
import { Venue } from '../interfaces/venue';
import { NeighbourhoodInfo } from '../interfaces/neighbourhood-info';

@Component({
  selector: 'app-venues',
  templateUrl: './neighbourhood-venues.component.html',
  styleUrls: ['./neighbourhood-venues.component.sass']
})
export class NeighbourhoodVenuesComponent implements OnInit {

  public venues: Venue[] | undefined;
  public neighbourhoodInfos: NeighbourhoodInfo[] | undefined;

  constructor(private nhVenuesService: NeighbourhoodVenuesService) { }

  ngOnInit() {
    this.getNeighbourhoodInfos();
  }

  public getVenues(): void {
    this.nhVenuesService.getVenues().subscribe(
      (response: Venue[]) => {
        this.venues = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    )
  }

  public getNeighbourhoodInfos(): void {
    this.nhVenuesService.getNeighbourhoodInfos().subscribe(
      (response: NeighbourhoodInfo[]) => {
        this.neighbourhoodInfos = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    )
  }
}
