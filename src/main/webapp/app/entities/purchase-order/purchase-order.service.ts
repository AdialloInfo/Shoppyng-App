import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { PurchaseOrder } from './purchase-order.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class PurchaseOrderService {

    private resourceUrl = SERVER_API_URL + 'api/purchase-orders';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(purchaseOrder: PurchaseOrder): Observable<PurchaseOrder> {
        const copy = this.convert(purchaseOrder);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(purchaseOrder: PurchaseOrder): Observable<PurchaseOrder> {
        const copy = this.convert(purchaseOrder);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<PurchaseOrder> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
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
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.order_date = this.dateUtils
            .convertLocalDateFromServer(entity.order_date);
    }

    private convert(purchaseOrder: PurchaseOrder): PurchaseOrder {
        const copy: PurchaseOrder = Object.assign({}, purchaseOrder);
        copy.order_date = this.dateUtils
            .convertLocalDateToServer(purchaseOrder.order_date);
        return copy;
    }
}
