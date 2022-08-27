import { Neighbourhood } from "./neighbourhood";
import { SlimReview } from "./slim-review";

export interface Venue {
    id: number,
    name: string,
    description?: string,
    infoUrl?: string,
    imgUrl?: string,
    streetAddress: string,
    postalCode: string,
    city: string,
    neighbourhood?: Neighbourhood,
    reviewList?: Array<SlimReview>,
    createdAt: Date,
    modifiedAt?: Date,
}