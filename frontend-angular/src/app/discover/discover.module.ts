import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NeighbourhoodVenuesComponent } from './neighbourhood-venues/neighbourhood-venues.component';
import { MatCardModule } from '@angular/material/card';
import { DiscoverRoutingModule } from './discover-routing.module';

@NgModule({
  declarations: [
    NeighbourhoodVenuesComponent,
  ],
  imports: [
    CommonModule,
    DiscoverRoutingModule,
    MatCardModule
  ],
  exports: [
    NeighbourhoodVenuesComponent,
  ]
})
export class DiscoverModule { }
