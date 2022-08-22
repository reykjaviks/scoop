import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from "./home/home.component";
import {VenueComponent} from "./venue/venue.component";
import {LoginComponent} from "./account/login/login.component";
import {RegisterComponent} from "./account/register/register.component";

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'venue', component: VenueComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'venues', loadChildren: () => import('./venues/venues.module').then(m => m.VenuesModule) },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
