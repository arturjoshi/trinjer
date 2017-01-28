/**
 * Created by Andrew Zelenskiy on 27.01.2017.
 */

const gulp = require('gulp');
const del = require('del');
const ts = require('gulp-typescript');
const browserSync = require('browser-sync');
const scss = require('gulp-sass');

const path = {
    src: 'src/',
    app: 'src/app/',
    build: 'dist/',
    ts: 'src/**/*.ts',
    html: 'src/**/*.html',
    scss: 'src/**/*.scss',
    systemjsConfig: 'src/systemjs.config.js'
};
const tsConfig = {
    app: path.app + 'tsconfig.json'
};
const project = ts.createProject(tsConfig.app);


gulp.task('clean', function(){
    return del([path.build]);
});

gulp.task('build-ts', function(){
    return gulp.src(path.ts)
        .pipe(project())
        .pipe(gulp.dest(path.build))
        .pipe(browserSync.stream());
});

gulp.task('build-html', function(){
    return gulp.src(path.html)
        .pipe(gulp.dest(path.build))
        .pipe(browserSync.stream());
});

gulp.task('copy-config', function(){
    return gulp.src(path.systemjsConfig)
        .pipe(gulp.dest(path.build))
        .pipe(browserSync.stream());
});

gulp.task('build-scss', function(){
    return gulp.src(path.scss)
        .pipe(scss({
            "bundleExec": true
        }))
        .pipe(gulp.dest(path.build))
        .pipe(browserSync.stream());
});

gulp.task('build-src', ['clean'],function(){
    gulp.start(['build-ts', 'build-html', 'build-scss', 'copy-config']);
});


gulp.task('webserver', ['build-src'], function(){
    browserSync({
        server: {
            baseDir: path.build,
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