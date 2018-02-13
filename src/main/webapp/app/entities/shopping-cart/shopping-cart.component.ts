import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiAlertService } from 'ng-jhipster';

import { ShoppingCart } from './shopping-cart.model';
import { ShoppingCartService } from './shopping-cart.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-shopping-cart',
    templateUrl: './shopping-cart.component.html'
})
export class ShoppingCartComponent implements OnInit, OnDestroy {
shoppingCarts: ShoppingCart[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private shoppingCartService: ShoppingCartService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.shoppingCartService.getShoppingCartCurrentUser().subscribe(
            (res: ResponseWrapper) => {
                this.shoppingCarts = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInShoppingCarts();
    }

    getSumPrice() {
        if (this.shoppingCarts !== undefined && this.shoppingCarts !== null && this.shoppingCarts.length > 0) {
            return this.shoppingCarts.map((c) => c.price).reduce((sum, current) => sum + current);
        } else {
            return 0;
        }
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ShoppingCart) {
        return item.id;
    }
    registerChangeInShoppingCarts() {
        this.eventSubscriber = this.eventManager.subscribe('shoppingCartListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
