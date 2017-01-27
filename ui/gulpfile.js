/**
 * Created by Andrew Zelenskiy on 27.01.2017.
 */

const gulp = require('gulp');
const del = require('del');
const ts = require('gulp-typescript');
const browserSync = require('browser-sync');

const path = {
    src: 'src/',
    app: 'src/app/',
    build: 'dist/'
};
const tsConfig = {
    app: path.app + 'tsconfig.json'
};
const project = ts.createProject(tsConfig.app);


gulp.task('clean', function(){
    return del([path.build]);
});

gulp.task('build-ts', function(){
    return gulp.src(path.src + "**/*.ts")
        .pipe(project())
        .pipe(gulp.dest(path.build));
});

gulp.task('build-html', function(){
    return gulp.src(path.src + "**/*.html")
        .pipe(gulp.dest(path.build));
});

gulp.task('copy-config', function(){
    return gulp.src(path.src + "systemjs.config.js")
        .pipe(gulp.dest(path.build));
});

gulp.task('build-dev', ['build-ts', 'build-html', 'copy-config']);

gulp.task('webserver', function(){
    browserSync({
        server: {
            baseDir: path.build,
            routes: {
                '/node_modules' : './node_modules'
            }
        }
    })
});