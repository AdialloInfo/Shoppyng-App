import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { ShoppingCart } from './shopping-cart.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ShoppingCartService {

    private resourceUrl = SERVER_API_URL + 'api/shopping-carts';

    constructor(private http: Http) { }

    create(shoppingCart: ShoppingCart): Observable<ShoppingCart> {
        const copy = this.convert(shoppingCart);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    addProductToCart(shoppingCart: ShoppingCart): Observable<ShoppingCart> {
        const copy = this.convert(shoppingCart);
         return this.http.post(`${this.resourceUrl}/addProductToCart`, copy).map((res: Response) => {
            return res.json();
        });
    }

    getShoppingCartCurrentUser(): Observable<ResponseWrapper> {
        return this.http.get(`${this.resourceUrl}/currentUser`).map((res: Response) => this.convertResponse(res));
    }

    update(shoppingCart: ShoppingCart): Observable<ShoppingCart> {
        const copy = this.convert(shoppingCart);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<ShoppingCart> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(shoppingCart: ShoppingCart): ShoppingCart {
        const copy: ShoppingCart = Object.assign({}, shoppingCart);
        return copy;
    }
}
