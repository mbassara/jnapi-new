# JNapi

A cross-platform application for downloading movie subtitles. It currently
uses [Napiprojekt](https://www.napiprojekt.pl) as the subtitle provider, but additional providers
(e.g., [OpenSubtitles](https://www.opensubtitles.org)) can be added in the future.

------------------------------------------------------------------------------

### Building
```
./gradlew build
```

------------------------------------------------------------------------------

### Usage

```
java -jar build/libs/jnapi-1.0.jar [-c=<charset>] [-f=<fps>] [--format=<format>] [-l=<language>] -p=<path>

  -c, --charset=<charset>   Target charset of the subtitles (default: UTF-8).
  -f, --fps=<fps>           Frames per second (you can use Media**Info to get the FPS
                            of a movie file). If subtitle format conversion
                            is not required, you can skip this parameter.
      --format=<format>     Target format of the subtitles. Possible values:
                            MicroDVD, MPL2, SubRip (default).
  -l, --language=<language> Language of the subtitles to download. Possible
                            values: PL (default), ENG.
  -p, --path=<path>         Movie file path.

```

------------------------------------------------------------------------------

### Database

Internally, the application uses Napiprojekt to search for subtitles via an unofficial API.
This means that one day, this API may disappear without notice, rendering the app unusable.
Fortunately, it has been working reliably since 2013. :)

------------------------------------------------------------------------------

### Language

While Napiprojekt primarily provides subtitles in Polish, it is also possible to request English subtitles.
For popular movies, you are likely to find English subtitles as well.

------------------------------------------------------------------------------

### Format

Subtitles in Napiprojekt are stored in the format in which they were originally uploaded.
To ensure consistency, the app attempts to parse raw subtitles using all supported formats before converting them to the
format specified by the user.
The default format is **SubRip**, while other supported formats include **MicroDVD** and **MPL2**.

------------------------------------------------------------------------------

### FPS

**SubRip** and **MPL2** use timestamps to determine when subtitles should appear, while **MicroDVD** uses frame numbers.
Converting between time-based and frame-based subtitle formats requires an additional FPS parameter.
You may run the program without specifying FPS, and if an error occurs, rerun it with the correct FPS value.

If you are unsure of the FPS, you can check it using [MediaInfo](https://mediaarea.net/en/MediaInfo).

------------------------------------------------------------------------------

### Charset

The application uses the [ICU library](https://unicode-org.github.io/icu/userguide/icu4j/) to automatically detect the
character encoding of the source subtitles. By default, the output is written in **UTF-8**, but you can specify any
charset [supported by Java](https://docs.oracle.com/javase/8/docs/technotes/guides/intl/encoding.doc.html) using the
`--charset` parameter.

------------------------------------------------------------------------------

### Fun Fact

I wrote this application a long time ago while I was in university. Now, after 12 years, I’ve done a massive refactor,
rewriting it to be more aligned with current coding practices. I cherry-picked a couple of commits to this repository:
the first one is the current `master`, and the others are the original commits from 2013 :)