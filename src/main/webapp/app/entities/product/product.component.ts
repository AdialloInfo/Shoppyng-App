import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiAlertService } from 'ng-jhipster';

import { Product } from './product.model';
import { ProductService } from './product.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-product',
    templateUrl: './product.component.html'
})
export class ProductComponent implements OnInit, OnDestroy {
products: Product[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private productService: ProductService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.productService.query().subscribe(
            (res: ResponseWrapper) => {
                this.products = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInProducts();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Product) {
        return item.id;
    }
    registerChangeInProducts() {
        this.eventSubscriber = this.eventManager.subscribe('productListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
