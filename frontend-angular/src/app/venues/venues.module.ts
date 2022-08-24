import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VenuesRoutingModule } from './venues-routing.module';
import { VenuesComponent } from './venues.component';
import { MatCardModule } from '@angular/material/card'; 

@NgModule({
  declarations: [
    VenuesComponent
  ],
  imports: [
    CommonModule,
    VenuesRoutingModule,
    MatCardModule
  ]
})
export class VenuesModule { }
