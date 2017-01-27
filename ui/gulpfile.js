/**
 * Created by Andrew Zelenskiy on 27.01.2017.
 */

const gulp = require('gulp');
const del = require('del');
const ts = require('gulp-typescript');

const path = {
    src: 'app/',
    build: 'build/'
};
const tsConfig = {
    app: 'app/tsconfig.json'
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

gulp.task('build-dev', ['build-ts', 'build-html']);