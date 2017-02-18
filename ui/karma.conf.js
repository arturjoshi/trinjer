/**
 * Created by xoll on 06.01.2017.
 */
module.exports = function (config) {

    var appBase = 'dist/'; //transpiled js and map files
    var appSrcBase = 'dist/'; //sources ts files
    var appAssets = 'base/dist/';

    //Testing helpers
    var testingBase = "testing/";
    var testingSrcBase = "testing";

    config.set({
        basePath: '',
        frameworks: ['jasmine', 'es6-shim'],

        plugins: [
            require('karma-jasmine'),
            require('karma-es6-shim'),
            require('karma-coverage'),
            require('karma-phantomjs-launcher'),
            require('karma-chrome-launcher'),
            require('karma-jasmine-html-reporter')
        ],

        client: {
            buitPath: [appBase, testingBase],
            clearContext: false
        },

        customLaunchers: {
            Chrome_travis_ci: {
                base: 'Chrome',
                flags: ['--no-sandbox']
            },
            PhantomJS_custom: {
                base: 'PhantomJS',
                options: {
                    windowName: 'my-window',
                    settings: {
                        webSecurityEnabled: false
                    },
                },
                flags: ['--load-images=true'],
                debug: true
            }
        },

        phantomjsLauncher: {
            // Have phantomjs exit if a ResourceError is encountered (useful if karma exits without killing phantom)
            exitOnResourceError: true
        },

        files: [
            //Systemjs
            'node_modules/systemjs/dist/system.src.js',
            'node_modules/systemjs/dist/system-polyfills.js',

            // Polyfills
            'node_modules/core-js/client/shim.js',

            //zone.js
            'node_modules/zone.js/dist/zone.js',
            'node_modules/zone.js/dist/long-stack-trace-zone.js',
            'node_modules/zone.js/dist/proxy.js',
            'node_modules/zone.js/dist/sync-test.js',
            'node_modules/zone.js/dist/jasmine-patch.js',
            'node_modules/zone.js/dist/async-test.js',
            'node_modules/zone.js/dist/fake-async-test.js',

            // RxJs
            { pattern: 'node_modules/rxjs/**/*.js', included: false, watched: false },
            { pattern: 'node_modules/rxjs/**/*.js.map', included: false, watched: false },

            // Paths loaded via module imports:
            // Angular itself
            { pattern: 'node_modules/@angular/**/*.js', included: false, watched: false },
            { pattern: 'node_modules/@angular/**/*.js.map', included: false, watched: false },

            { pattern: 'src/systemjs.config.js', included: false, watched: false },
            { pattern: 'systemjs.config.extras.js', included: false, watched: false },
            'karma-test-shim.js', // optionally extend SystemJS mapping e.g., with barrels

            // transpiled application & spec code paths loaded via module imports
            { pattern: appBase + '**/*.js', included: false, watched: true },
            { pattern: testingBase + '**/*.js', included: false, watched: true },


            // Asset (HTML & CSS) paths loaded via Angular's component compiler
            // (these paths need to be rewritten, see proxies section)
            { pattern: appBase + '**/*.html', included: false, watched: true },
            { pattern: appBase + '**/*.css', included: false, watched: true },

            // Paths for debugging with source maps in dev tools
            { pattern: appSrcBase + '**/*.ts', included: false, watched: false },
            { pattern: appBase + '**/*.js.map', included: false, watched: false },
            { pattern: testingSrcBase + '**/*.ts', included: false, watched: false },
            { pattern: testingBase + '**/*.js.map', included: false, watched: false }
        ],

        // Proxied base paths for loading assets
        proxies: {
            // required for component assets fetched by Angular's compiler
            "/src/app/": appAssets
        },

        exclude: [],
        preprocessors: {
            // "dist/src/**/*.js": ['coverage'] 
        },
        reporters: ['progress', 'kjhtml'],
        // coverageReporter: {
        //     type : 'html',
        //     dir : 'coverage/'
        // },

        port: 9876,
        colors: true,
        logLevel: config.LOG_INFO,
        autoWatch: true,
        browsers: ['PhantomJS_custom'],
        singleRun: false
    })
};