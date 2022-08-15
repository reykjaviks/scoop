export interface VenueGetDTO {
  id: number,
  name: string,
  description?: string,
  infoUrl?: string,
  imgUrl?: string,
  streetAddress: string,
  postalCode: string,
  city: string,
  neighbourhood?: NeighbourhoodGetDTO,
  reviewList?: Array<ReviewSlimDTO>,
  createdAt: Date,
  modifiedAt?: Date,
}

export interface ReviewSlimDTO {
  id: number,
  review: String,
  rating: number,
  user: UserSlimDTO,
  createdAt: Date,
  modifiedAt?: Date,
}

export interface NeighbourhoodGetDTO {
  id: number,
  name: string,
}

export interface UserSlimDTO {
  id: number,
  username: string,
}
