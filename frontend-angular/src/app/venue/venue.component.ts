import { Component, OnInit } from '@angular/core';
import {VenueGetDTO} from "../core/data";
import {VenueService} from "./venue.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-venue',
  templateUrl: './venue.component.html',
  styleUrls: ['./venue.component.sass']
})
export class VenueComponent implements OnInit {
  public venues: VenueGetDTO[] | undefined;

  constructor(private venueService: VenueService) {}

  ngOnInit() {
    this.getVenues();
  }

  public getVenues(): void {
    this.venueService.getVenues().subscribe(
      (response: VenueGetDTO[]) => {
        this.venues = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    )
  }

}
