import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ShoppyngAppSharedModule } from '../../shared';
import {
    ShoppingCartService,
    ShoppingCartPopupService,
    ShoppingCartComponent,
    ShoppingCartDetailComponent,
    ShoppingCartDialogComponent,
    ShoppingCartPopupComponent,
    ShoppingCartDeletePopupComponent,
    ShoppingCartDeleteDialogComponent,
    shoppingCartRoute,
    shoppingCartPopupRoute,
} from './';

const ENTITY_STATES = [
    ...shoppingCartRoute,
    ...shoppingCartPopupRoute,
];

@NgModule({
    imports: [
        ShoppyngAppSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ShoppingCartComponent,
        ShoppingCartDetailComponent,
        ShoppingCartDialogComponent,
        ShoppingCartDeleteDialogComponent,
        ShoppingCartPopupComponent,
        ShoppingCartDeletePopupComponent,
    ],
    entryComponents: [
        ShoppingCartComponent,
        ShoppingCartDialogComponent,
        ShoppingCartPopupComponent,
        ShoppingCartDeleteDialogComponent,
        ShoppingCartDeletePopupComponent,
    ],
    providers: [
        ShoppingCartService,
        ShoppingCartPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ShoppyngAppShoppingCartModule {}
