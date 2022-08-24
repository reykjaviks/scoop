create materialized view scoop.neighbourhood_images as
    with neighbourhood_images as (
        select
            neighbourhood_id,
            img_url,
            row_number() over (
                partition by neighbourhood_id order by random()
            ) as row_num
        from scoop.venue
        where neighbourhood_id is not null
        and img_url is not null
    )
    select
        nh.id as neighbourhood_id,
        nh.name as neighbourhood_name,
        images.img_url
    from neighbourhood_images images
    inner join scoop.neighbourhood nh on nh.id = images.neighbourhood_id
    where images.row_num = 1;

-- Access
grant select on scoop.neighbourhood_images to scoopuser;