/**
 * System configuration for Angular 2 samples
 * Adjust as necessary for your application needs.
 */
(function (global) {
    System.config({
    "defaultJSExtensions": true,
    paths: {
    	'npm:': 'node_modules/',
    },
    // map tells the System loader where to look for things
    map: {
      // other libraries
        'rxjs': 'npm:rxjs',
        game: './',
    },
    // packages tells the System loader how to load when no filename and/or no extension
    packages: {
      rxjs: {
        defaultExtension: 'js'
      },
      game: {
          main: './main.js',
          defaultExtension: 'js'
      }
    },
    baseURL: '/',

  });
})(this);