/*---------------------- Functions ----------------------*/
-- Updating modified_at
create function scoop.update_modified_at()
returns trigger as $$
	begin
		new.modified_at = now();
		return new;
	end;
$$ language plpgsql;

/*---------------------- Triggers ----------------------*/
-- User
create trigger before_update
before update on scoop.user 
for each row execute procedure scoop.update_modified_at();

-- Authority
create trigger before_update
before update on scoop.authority 
for each row execute procedure scoop.update_modified_at();

-- Userauthority
create trigger before_update
before update on scoop.userauthority 
for each row execute procedure scoop.update_modified_at();

-- Csrftoken
create trigger before_update
before update on scoop.csrftoken 
for each row execute procedure scoop.update_modified_at();

-- Venue
create trigger before_update
before update on scoop.venue 
for each row execute procedure scoop.update_modified_at();

-- Review
create trigger before_update
before update on scoop.review 
for each row execute procedure scoop.update_modified_at();

-- Neighbourhood
create trigger before_update
before update on scoop.neighbourhood 
for each row execute procedure scoop.update_modified_at();