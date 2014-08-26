# hoplon-tops

A Hoplon project with Castra designed to show optmistic render on the client
side while asynchronously calling the server for validation. This project
also has tests using [purnam.test][5] and [double-check][7], which are run
on [Karma][6].

The client pools the server each second for a random word and shows the last
10 words. The user can submit new words, which will appear on the list
immediately and submited to the server.

The server has an artificial delay of 2 seconds before returning a response.
If the word has more than 6 characteres the server will tell that is a
invalid word and the client will show in a different manner valid and invalid
words.

## Dependencies

- java 1.7+
- [boot][1]
- [leiningen][2]
- [npm][4] (only for testing)

## Usage

1. Start the auto-compiler. In a terminal:

    ```bash
    $ boot dev
    ```

2. Go to [http://localhost:8000][3] in your browser.

## Testing
You need to install karma and some plugins before running the tests. In a terminal:

```bash
npm install karma karma-jasmine karma-chrome-launcher karma-firefox-launcher
```

You should also install karma-cli because it makes your life easier. In a terminal (you may need sudo):
```bash
npm install -g karma-cli
```

Now to run the tests:

1. Start the auto-compiler. In a terminal:

    ```bash
    $ boot test
    ```

2. Start the karma runner. In another terminal:

    ```bash
    $ karma start
    ```

3. When you save a file, hoplon will auto compile and then karma will auto
run the tests.

## License

Copyright © 2014, Marcelo Nomoto. All rights reserved.
```
The use and distribution terms for this software are covered by the Eclipse
Public License 1.0 (http://opensource.org/licenses/EPL-1.0) which can
be found in the file epl-v10.html at the root of this distribution. By using
this software in any fashion, you are agreeing to be bound by the terms of
this license. You must not remove this notice, or any other, from this software.
```
[1]: https://github.com/tailrecursion/boot
[2]: https://github.com/technomancy/leiningen
[3]: http://localhost:8000
[4]: https://www.npmjs.org
[5]: http://purnam.github.io/purnam.test
[6]: https://karma-runner.github.io/0.12/index.html
[7]: https://github.com/cemerick/double-check
