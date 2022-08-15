import { useEffect, useState } from "react";
import APIService from "./APIService";

function Neighbourhood() {
    const [neighbourhoods, setNeighbourhoods] = useState([])
    console.log(neighbourhoods)

    useEffect(() => {
        APIService.getNeighbourhoods().then(data => setNeighbourhoods(data))
    },[])

    return (
        <div className="Neighbourhood">
            <header>
                <h2>List of all neighbourhoods</h2>
                {neighbourhoods?.map(nbh => <div>{nbh.name}</div>)}
            </header>
        </div>
    );
}

export default Neighbourhood;