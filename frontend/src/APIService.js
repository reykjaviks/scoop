const NEIGHBOURHOODS_ENDPOINT = 'http://localhost:9090/api/neighbourhood/all';
class APIService {
    getNeighbourhoods() {
        return fetch(NEIGHBOURHOODS_ENDPOINT,{
            method: 'get',
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'Content-Type': 'application/json',
                'REQUEST-ID': 'TESTING',
                'X-IDENTIFIER': 'Elizabeth'
            },
            'credentials': 'same-origin'
        }).then(res => res.json())
    }
}

export default new APIService();

