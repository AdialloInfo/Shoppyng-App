import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ShoppingCart } from './shopping-cart.model';
import { ShoppingCartPopupService } from './shopping-cart-popup.service';
import { ShoppingCartService } from './shopping-cart.service';

@Component({
    selector: 'jhi-shopping-cart-delete-dialog',
    templateUrl: './shopping-cart-delete-dialog.component.html'
})
export class ShoppingCartDeleteDialogComponent {

    shoppingCart: ShoppingCart;

    constructor(
        private shoppingCartService: ShoppingCartService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.shoppingCartService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'shoppingCartListModification',
                content: 'Deleted an shoppingCart'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-shopping-cart-delete-popup',
    template: ''
})
export class ShoppingCartDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private shoppingCartPopupService: ShoppingCartPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.shoppingCartPopupService
                .open(ShoppingCartDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
