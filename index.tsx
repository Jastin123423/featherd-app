import React, { Component, ErrorInfo, ReactNode } from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';

import AppRouter from './AppRouter';  // ✅ Changed from App to AppRouter

import { LanguageProvider } from './contexts/LanguageContext';
import { setupUneraPush } from './firebase';
import { initAndroidBridge, isNativeApp } from './utils/androidBridge';

// Initialize native bridge features when on Android
initAndroidBridge();

interface ErrorBoundaryProps {
  children: any;
}

interface ErrorBoundaryState {
  hasError: boolean;
  error: Error | null;
}

class RootErrorBoundary extends (React.Component as any) {
  state: ErrorBoundaryState = { hasError: false, error: null };

  constructor(props: any) {
    super(props);
  }

  static getDerivedStateFromError(error: Error): ErrorBoundaryState {
    return { hasError: true, error };
  }

  componentDidCatch(error: Error, errorInfo: any) {
    console.error('Feathered Root Caught Error:', error, errorInfo);
  }

  render() {
    if ((this.state as ErrorBoundaryState).hasError) {
      return (
        <div className="min-h-screen bg-[#050B18] text-[#F8FAFC] flex flex-col items-center justify-center p-6 text-center select-none font-sans">
          <div className="w-16 h-16 rounded-2xl bg-[#0F172A] border border-[#1E293B] flex items-center justify-center mb-6 shadow-xl">
            <span className="text-2xl text-[#F97316]">✦</span>
          </div>
          <h1 className="text-2xl font-bold mb-2">Feathered</h1>
          <p className="text-[#94A3B8] max-w-md text-sm mb-6">
            Something temporarily went wrong while rendering this screen.
          </p>
          <button
            onClick={() => {
              (this as any).setState({ hasError: false, error: null });
              window.location.href = '/';
            }}
            className="px-6 py-3 bg-[#1877F2] hover:bg-[#166FE5] active:scale-95 text-white font-semibold rounded-xl transition-all shadow-lg"
          >
            Return to Feed
          </button>
        </div>
      );
    }
    return (this.props as any).children;
  }
}

const rootElement =
  document.getElementById('root');

if (!rootElement) {

  throw new Error(
    'Could not find root element to mount to'
  );
}

const root =
  ReactDOM.createRoot(rootElement);

root.render(
  <React.StrictMode>
    <RootErrorBoundary>
      <BrowserRouter>
        <LanguageProvider>
          <AppRouter />
        </LanguageProvider>
      </BrowserRouter>
    </RootErrorBoundary>
  </React.StrictMode>
);

/* =========================================
   UNERA SERVICE WORKER
========================================= */

if (!isNativeApp() && 'serviceWorker' in navigator) {

  window.addEventListener(
    'load',
    async () => {

      try {

        /* =========================================
           MAIN PWA SERVICE WORKER
        ========================================= */

        const registration =
          await navigator
            .serviceWorker
            .register('/sw.js');

        console.log(
          '✅ UNERA SW registered:',
          registration.scope
        );

        /* =========================================
           FIREBASE MESSAGING SW
        ========================================= */

        await navigator.serviceWorker.register(
          '/firebase-messaging-sw.js'
        );

        console.log(
          '🔥 Firebase Messaging SW registered'
        );

        /* =========================================
           SETUP PUSH NOTIFICATIONS
        ========================================= */

        await setupUneraPush();

        /* =========================================
           FORCE WAITING SW TO ACTIVATE
        ========================================= */

        if (registration.waiting) {

          registration.waiting.postMessage({
            type: 'SKIP_WAITING',
          });
        }

        /* =========================================
           LISTEN FOR NEW SW
        ========================================= */

        registration.addEventListener(
          'updatefound',
          () => {

            const newWorker =
              registration.installing;

            if (!newWorker) return;

            console.log(
              '⬇️ New UNERA update found'
            );

            newWorker.addEventListener(
              'statechange',
              () => {

                console.log(
                  'SW STATE:',
                  newWorker.state
                );

                if (
                  newWorker.state ===
                    'installed' &&
                  navigator
                    .serviceWorker
                    .controller
                ) {

                  console.log(
                    '🔄 UNERA updated'
                  );

                  /* =========================================
                     UPDATE EVENT
                  ========================================= */

                  window.dispatchEvent(
                    new CustomEvent(
                      'uneraUpdateAvailable'
                    )
                  );

                  /* =========================================
                     AUTO REFRESH
                  ========================================= */

                  window.location.reload();
                }
              }
            );
          }
        );

        /* =========================================
           SW MESSAGE CHANNEL
        ========================================= */

        navigator.serviceWorker.addEventListener(
          'message',
          (event) => {

            console.log(
              '📩 SW MESSAGE:',
              event.data
            );

            window.dispatchEvent(
              new CustomEvent(
                'uneraSWMessage',
                {
                  detail: event.data,
                }
              )
            );
          }
        );

        /* =========================================
           ONLINE EVENT
        ========================================= */

        window.addEventListener(
          'online',
          () => {

            console.log(
              '🌐 UNERA back online'
            );

            window.dispatchEvent(
              new CustomEvent(
                'uneraOnline'
              )
            );
          }
        );

        /* =========================================
           OFFLINE EVENT
        ========================================= */

        window.addEventListener(
          'offline',
          () => {

            console.log(
              '📴 UNERA offline'
            );

            window.dispatchEvent(
              new CustomEvent(
                'uneraOffline'
              )
            );
          }
        );

        /* =========================================
           PWA INSTALL READY
        ========================================= */

        window.addEventListener(
          'beforeinstallprompt',
          (event: any) => {

            console.log(
              '📲 UNERA install available'
            );

            event.preventDefault();

            (window as any)
              .deferredPrompt = event;

            window.dispatchEvent(
              new CustomEvent(
                'uneraInstallReady'
              )
            );
          }
        );

        /* =========================================
           PWA INSTALLED
        ========================================= */

        window.addEventListener(
          'appinstalled',
          () => {

            console.log(
              '🎉 UNERA installed'
            );

            window.dispatchEvent(
              new CustomEvent(
                'uneraInstalled'
              )
            );
          }
        );

      } catch (err) {

        console.error(
          '❌ UNERA SW registration failed:',
          err
        );
      }
    }
  );
}
