import { Component, OnInit } from '@angular/core';
import { Venue } from './interfaces/venue';
import { VenuesService } from './venues.service';
import { HttpErrorResponse } from "@angular/common/http";
import { NeighbourhoodInfo } from './interfaces/neighbourhood-info';

@Component({
  selector: 'app-venues',
  templateUrl: './venues.component.html',
  styleUrls: ['./venues.component.sass']
})
export class VenuesComponent implements OnInit {

  public venues: Venue[] | undefined;
  public neighbourhoodInfos: NeighbourhoodInfo[] | undefined;

  constructor(private venuesService: VenuesService) { }

  ngOnInit() {
    this.getNeighbourhoodInfos();
  }

  public getVenues(): void {
    this.venuesService.getVenues().subscribe(
      (response: Venue[]) => {
        this.venues = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    )
  }

  public getNeighbourhoodInfos(): void {
    this.venuesService.getNeighbourhoodInfos().subscribe(
      (response: NeighbourhoodInfo[]) => {
        this.neighbourhoodInfos = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    )
  }
}
