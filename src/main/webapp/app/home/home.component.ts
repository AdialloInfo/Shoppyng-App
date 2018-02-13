import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Product } from '../entities/product/product.model';
import { ShoppingCart } from '../entities/shopping-cart/shopping-cart.model';
import { ProductService } from '../entities/product/product.service';
import { ShoppingCartService } from '../entities/shopping-cart/shopping-cart.service';
import { Observable } from 'rxjs/Rx';
import { Router } from '@angular/router';

import { Account, LoginModalService, Principal, ResponseWrapper } from '../shared';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.css'
    ]

})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    products: Product[];

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private productService: ProductService,
        private shoppingCartService: ShoppingCartService,
        private alertService: JhiAlertService,
        private router: Router
    ) {
    }

    ngOnInit() {
        this.loadAllProduct();
        this.principal.identity().then((account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
            });
        });
    }

    addToCart(product: Product, quantity: number) {
        if (this.isAuthenticated()) {
            const shoppingCart = new ShoppingCart();
            shoppingCart.product = product;
            shoppingCart.quantity = quantity;
            this.shoppingCartService.addProductToCart(shoppingCart).subscribe(
                (res: ShoppingCart) => this.router.navigate(['shopping-cart']),
                (res: Response) => this.onError(res.json));
        } else {
            this.login();
        }
    }

    private subscribeToSaveResponse(result: Observable<ShoppingCart>) {
        result.subscribe((res: ShoppingCart) =>
            console.log('ok ok'), (res: Response) => console.log('ok ok'));
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    loadAllProduct() {
        this.productService.query().subscribe(
            (res: ResponseWrapper) => {
                this.products = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
