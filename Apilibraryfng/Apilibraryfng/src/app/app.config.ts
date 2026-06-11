import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideHttpClient } from '@angular/common/http';
import { provideApiConfiguration } from './api/api-configuration';
import { environment } from './environments/environments';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeuix/themes/aura';
import { definePreset } from '@primeuix/themes';
import { ConfirmationService, MessageService } from 'primeng/api';

const MyPreset = definePreset(Aura, {});

export const appConfig: ApplicationConfig = {
    providers: [
        provideBrowserGlobalErrorListeners(),
        provideRouter(routes),
        provideHttpClient(),
        provideApiConfiguration(environment.urlBase),
        providePrimeNG({
            theme: {
                preset: MyPreset,
                options: { darkModeSelector: '.my-app-dark' }
            }
        }),
        MessageService,
        ConfirmationService
    ]
};