import { SlimUser } from "./slim-user";

export interface SlimReview {
    id: number,
    review: String,
    rating: number,
    user: SlimUser,
    createdAt: Date,
    modifiedAt?: Date,
  }