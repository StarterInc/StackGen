import React, {Component} from "react";
import { LiveProvider, LiveEditor, LiveError, LivePreview } from "react-live";
import { MDXProvider } from '@mdx-js/tag'

const MyCodeComponent = ({ children, ...props }) => (
  <LiveProvider code={children}>
    <LiveEditor />
    <LiveError />
    <LivePreview />
  </LiveProvider>
);

export default class MyPageLayout extends Component {
  render() {
    return <MDXProvider components={{code: MyCodeComponent}}>
      <div>{this.props.children}</div>
    </MDXProvider>
  }
}
