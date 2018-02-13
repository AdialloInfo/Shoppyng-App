import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { PurchaseOrder } from './purchase-order.model';
import { PurchaseOrderPopupService } from './purchase-order-popup.service';
import { PurchaseOrderService } from './purchase-order.service';

@Component({
    selector: 'jhi-purchase-order-delete-dialog',
    templateUrl: './purchase-order-delete-dialog.component.html'
})
export class PurchaseOrderDeleteDialogComponent {

    purchaseOrder: PurchaseOrder;

    constructor(
        private purchaseOrderService: PurchaseOrderService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.purchaseOrderService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'purchaseOrderListModification',
                content: 'Deleted an purchaseOrder'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-purchase-order-delete-popup',
    template: ''
})
export class PurchaseOrderDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private purchaseOrderPopupService: PurchaseOrderPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.purchaseOrderPopupService
                .open(PurchaseOrderDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
