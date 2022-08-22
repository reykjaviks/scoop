import { Component, OnInit } from '@angular/core';
import { Venue } from './interfaces/venue';
import { VenuesService } from './venues.service';
import { HttpErrorResponse } from "@angular/common/http";

@Component({
  selector: 'app-venues',
  templateUrl: './venues.component.html',
  styleUrls: ['./venues.component.sass']
})
export class VenuesComponent implements OnInit {

  public venues: Venue[] | undefined;

  constructor(private venuesService: VenuesService) { }

  ngOnInit() {
    this.getVenues();
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
}
