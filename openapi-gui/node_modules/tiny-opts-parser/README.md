An extremely lightweight CLI options parser.

Install via npm or yarn

```console
> npm install --save tiny-opts-parser
```

And then just use `import` (or `require` if you need to):

```js
import parser from 'tiny-opts-parser';
```

In node, command line input is passed to application as an array. Tiny Opts Parser takes this array as its first argument, and an options object as its (optional) second argument. Tiny Opts Parser returns an object that maps the name of each argument or option to its value. The output will be exactly the same as the output of all the popular JS CLI parsers like Commander, Minimist, and Yargs.

For those who are new to CLI options parsing, the process can get quite complicated. For example, if someone types the following on the command line: `mycommand -a -bcd def xyz --abcd`, the parser needs to know turn this into the following object:
```js
{
  _: ['xyz'],
  a: true,
  b: true,
  c: true,
  d: 'def',
  abcd: true,
}
```
