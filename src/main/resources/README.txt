# WELCOME to the csia Note App
### Before you can sync files with Dropbox you must authorize your account through the 'Sync' menu.

* Files will sync automatically from Dropbox upon launch (if there is wifi).
* You can manually sync up with Dropbox from the 'Sync' menu.

Supported Keyboard shortcuts:

	Ctrl+S: Saves current file (will default to 'Save as'... if there is no existing file with that name.)

	Ctrl+Shift+S: Brings up the 'Save as...' window.

	Ctrl+W: Closes current tab.

	Ctrl+Q: Closes the main application window.

Files also have support for **markdown** through the *flexmark* java library extension

For help with using markdown, see [this helpful guide](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet).



You can also #tag# information to be searched through later.

To tag a file place a hash-tag before and after the what you want to call your tag.

This will tag any information you place directly below the tag, as separated by two new lines.

If you would like to tag more information than this, utilize the *end tag* feature. in which
you create another tag of the same name where you wish it to end, but mark this with a '/' just before the tag name.

Additionally, you can record mathematical notes with MathJax<sup>TM</sup>. By default, MathJax parsing is disabled as it is not as smooth to load.
However, you can toggle this feature, as well as other types of rendering, in the 'Rendering' menu selection.

An example of MathJax can be seen below. Give rendering it a try!

When $a \ne 0$, there are two solutions to $ ax^2 + bx + c = 0 $ and they are
$$
x = {-b \pm \sqrt{b^2-4ac} \over 2a}.
$$

If you would like to stop rendering, you can disable text rendering from the menu as well as pause the currently rendered screen.
For a guide on how to use MathJax, visit the [MathJax homepage](https://www.mathjax.org/)