import {Component, OnInit} from '@angular/core';
import {VenueGetDTO} from "./common/data";
import {VenueService} from "./venue/venue.service";
import {HttpErrorResponse} from "@angular/common/http";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent implements OnInit {
  public venues: VenueGetDTO[] | undefined;

  constructor(private venueService: VenueService, private modalService: NgbModal) {}

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

  public open(modal: any): void {
    this.modalService.open(modal)

  }
}
