import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiAlertService } from 'ng-jhipster';

import { PurchaseOrder } from './purchase-order.model';
import { PurchaseOrderService } from './purchase-order.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-purchase-order',
    templateUrl: './purchase-order.component.html'
})
export class PurchaseOrderComponent implements OnInit, OnDestroy {
purchaseOrders: PurchaseOrder[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private purchaseOrderService: PurchaseOrderService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.purchaseOrderService.query().subscribe(
            (res: ResponseWrapper) => {
                this.purchaseOrders = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInPurchaseOrders();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: PurchaseOrder) {
        return item.id;
    }
    registerChangeInPurchaseOrders() {
        this.eventSubscriber = this.eventManager.subscribe('purchaseOrderListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
