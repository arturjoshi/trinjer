/**
 * Created by Andrew Zelenskiy on 27.01.2017.
 */

const gulp = require('gulp');
const del = require('del');
const ts = require('gulp-typescript');
const browserSync = require('browser-sync');
const scss = require('gulp-sass');
const karmaServer = require('karma').Server;
const runSequence = require('run-sequence');
const modRewrite = require("connect-modrewrite");

const path = {
    src: 'src/',
    app: 'src/app/',
    build: 'dist/',
    test: 'test/',
    buildSrc: 'dist/src/',
    ts: 'src/**/*.ts',
    html: 'src/**/*.html',
    scss: 'src/**/*.scss',
    testTs: 'test/**.ts',
    systemjsConfig: 'src/systemjs.config.js'
};
const tsConfig = {
    app: path.app + 'tsconfig.json'
};
const srcProject = ts.createProject(tsConfig.app);
const testProject = ts.createProject(tsConfig.app);


gulp.task('clean', function(){
    return del([path.build]);
});

gulp.task('build-ts', function(){
    return gulp.src(path.ts)
        .pipe(srcProject())
        .pipe(gulp.dest(path.buildSrc))
        .pipe(browserSync.stream());
});

gulp.task('build-html', function(){
    return gulp.src(path.html)
        .pipe(gulp.dest(path.buildSrc))
        .pipe(browserSync.stream());
});

gulp.task('copy-config', function(){
    return gulp.src(path.systemjsConfig)
        .pipe(gulp.dest(path.buildSrc))
        .pipe(browserSync.stream());
});

gulp.task('build-scss', function(){
    return gulp.src(path.scss)
        .pipe(scss({
            "bundleExec": true
        }))
        .pipe(gulp.dest(path.buildSrc))
        .pipe(browserSync.stream());
});

gulp.task('build-src', function(){
    return runSequence('clean', ['build-ts', 'build-html', 'build-scss', 'copy-config']);
});

gulp.task('build-test', function(){
    return gulp.src(path.test + "**/*.spec.ts")
        .pipe(testProject())
        .pipe(gulp.dest(path.build + "/test/"));
});

gulp.task('build-dev-full', function(){
    runSequence('clean', ['build-ts', 'build-html', 'build-scss', 'copy-config'], 'build-test');
});

gulp.task('test', function(){
    runSequence('build-dev-full', 'start-karma-server');
});

gulp.task('start-karma-server', function(){
    new karmaServer({
        configFile: __dirname + "/karma.conf.js",
        singleRun: false
    }).start();

    gulp.watch(path.ts, ['build-ts']);
    gulp.watch(path.test + "**/*.ts", ['build-test']);
});

gulp.task('webserver', ['build-src'], function(){
    browserSync({
        port: 9009,
        server: {
            baseDir: path.buildSrc,
            middleware: [
                modRewrite([
                    '!\\.\\w+$ /index.html [L]'
                ])
            ],
            routes: {
                '/node_modules' : './node_modules'
            }
        }
    });

    gulp.watch(path.ts, ['build-ts']);
    gulp.watch(path.html, ['build-html']);
    gulp.watch(path.scss, ['build-scss']);
    gulp.watch(path.systemjsConfig, ['copy-config']);
});