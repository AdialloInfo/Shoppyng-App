import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ShoppingCartComponent } from './shopping-cart.component';
import { ShoppingCartDetailComponent } from './shopping-cart-detail.component';
import { ShoppingCartPopupComponent } from './shopping-cart-dialog.component';
import { ShoppingCartDeletePopupComponent } from './shopping-cart-delete-dialog.component';

export const shoppingCartRoute: Routes = [
    {
        path: 'shopping-cart',
        component: ShoppingCartComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Shopping Cart'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'shopping-cart/:id',
        component: ShoppingCartDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ShoppingCarts'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const shoppingCartPopupRoute: Routes = [
    {
        path: 'shopping-cart-new',
        component: ShoppingCartPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ShoppingCarts'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'shopping-cart/:id/edit',
        component: ShoppingCartPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ShoppingCarts'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'shopping-cart/:id/delete',
        component: ShoppingCartDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ShoppingCarts'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
