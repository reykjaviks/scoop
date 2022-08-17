import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from "./home/home.component";
import {VenueComponent} from "./venue/venue.component";

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'venues', component: VenueComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
