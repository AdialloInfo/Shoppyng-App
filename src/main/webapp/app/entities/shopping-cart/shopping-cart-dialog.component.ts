import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ShoppingCart } from './shopping-cart.model';
import { ShoppingCartPopupService } from './shopping-cart-popup.service';
import { ShoppingCartService } from './shopping-cart.service';
import { PurchaseOrder, PurchaseOrderService } from '../purchase-order';
import { Product, ProductService } from '../product';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-shopping-cart-dialog',
    templateUrl: './shopping-cart-dialog.component.html'
})
export class ShoppingCartDialogComponent implements OnInit {

    shoppingCart: ShoppingCart;
    isSaving: boolean;

    purchaseorders: PurchaseOrder[];

    products: Product[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private shoppingCartService: ShoppingCartService,
        private purchaseOrderService: PurchaseOrderService,
        private productService: ProductService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.purchaseOrderService
            .query({filter: 'shoppingcart-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.shoppingCart.purchaseOrder || !this.shoppingCart.purchaseOrder.id) {
                    this.purchaseorders = res.json;
                } else {
                    this.purchaseOrderService
                        .find(this.shoppingCart.purchaseOrder.id)
                        .subscribe((subRes: PurchaseOrder) => {
                            this.purchaseorders = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.productService
            .query({filter: 'shoppingcart-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.shoppingCart.product || !this.shoppingCart.product.id) {
                    this.products = res.json;
                } else {
                    this.productService
                        .find(this.shoppingCart.product.id)
                        .subscribe((subRes: Product) => {
                            this.products = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.shoppingCart.id !== undefined) {
            this.subscribeToSaveResponse(
                this.shoppingCartService.update(this.shoppingCart));
        } else {
            this.subscribeToSaveResponse(
                this.shoppingCartService.create(this.shoppingCart));
        }
    }

    private subscribeToSaveResponse(result: Observable<ShoppingCart>) {
        result.subscribe((res: ShoppingCart) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: ShoppingCart) {
        this.eventManager.broadcast({ name: 'shoppingCartListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    trackPurchaseOrderById(index: number, item: PurchaseOrder) {
        return item.id;
    }

    trackProductById(index: number, item: Product) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-shopping-cart-popup',
    template: ''
})
export class ShoppingCartPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private shoppingCartPopupService: ShoppingCartPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.shoppingCartPopupService
                    .open(ShoppingCartDialogComponent as Component, params['id']);
            } else {
                this.shoppingCartPopupService
                    .open(ShoppingCartDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
