/**
 *
 * Csat Data Entry FORM
 *
 *
 *
 *
 *
 *
 *
 * var Csat:{  id: '',
 *  userId: '',
 *  comments: '',
 *  contentId: '',
 *  rating: '',
 *  }
 */
import React, { useState } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { Toast, Row, Form, Button } from "react-bootstrap";
import { FaChevronUp } from "react-icons/fa";
import { addCsat, resetCsat } from "../../actions/Csats";
import Csat from "./Csat";

import "./style.css";

const PopUpAlert = props => {
  const [show, setShow] = useState(false);

  if (show) {
    return (
      <div>
        <Toast
          style={{
            position: "absolute",
            minWidth: "400px",
            top: -300,
            right: 150
          }}
          // className={"csatQuestionnaire"}
        >
          <Toast.Header onClick={() => setShow(false)}>
            <img
              src="holder.js/20x20?text=%20"
              className="rounded mr-2"
              alt=""
            />
            <h5 className="mr-auto">Please Rate This Page</h5>
            <small />
          </Toast.Header>
          <Toast.Body>{props.children}</Toast.Body>
        </Toast>
      </div>
    );
  } else {
    return (
      <Button
        style={{
          position: "absolute",
          right: 150
        }}
        onClick={() => setShow(true)}
      >
        <b>Leave Feedback</b>&nbsp;&nbsp;
        <FaChevronUp />
      </Button>
    );
  }
};

class CsatQuestionnaire extends React.Component {
  constructor(props) {
    super(props);
    this.onuserIdChange = this.onuserIdChange.bind(this);
    this.oncommentsChange = this.oncommentsChange.bind(this);
    this.oncontentIdChange = this.oncontentIdChange.bind(this);
    this.onratingChange = this.onratingChange.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.onrating4 = this.onrating4.bind(this);
    this.onrating3 = this.onrating3.bind(this);
    this.onrating2 = this.onrating2.bind(this);
    this.onrating1 = this.onrating1.bind(this);
    this.onrating0 = this.onrating0.bind(this);
    this.state = {
      userId: props.Csat ? props.Csat.userId : 100,
      comments: props.Csat ? props.Csat.comments : "",
      contentId: props.Csat ? props.Csat.contentId : 0,
      rating: props.Csat ? props.Csat.rating : 0,
      error: "",
      validated: !true
    };
  }

  /**
   * Csat
   * Data Action Event Listener
   *
   */
  onuserIdChange(e) {
    const userIdval = e.target.value;
    this.setState(() => ({ userId: userIdval }));
  }
  /**
   * Csat
   * Data Action Event Listener
   *
   */
  oncommentsChange(e) {
    const commentsval = e.target.value;
    this.setState(() => ({ comments: commentsval }));
  }
  /**
   * Csat
   * Data Action Event Listener
   *
   */
  oncontentIdChange(e) {
    const contentIdval = e.target.value;
    this.setState(() => ({ contentId: contentIdval }));
  }
  /**
   * Csat
   * Data Action Event Listener
   *
   */
  onratingChange(e) {
    const ratingval = e.target.value;
    this.setState(() => ({ rating: ratingval }));
  }

  onrating4() {
    this.setState(() => ({ rating: 10 }));
  }

  onrating3() {
    this.setState(() => ({ rating: 8 }));
  }

  onrating2() {
    this.setState(() => ({ rating: 5 }));
  }

  onrating1() {
    this.setState(() => ({ rating: 2 }));
  }

  onrating0() {
    this.setState(() => ({ rating: 0 }));
  }

  componentDidUpdate(previousProps, previousState) {
    if (previousProps.submitting && !this.props.errorMessage) {
      this.props.dispatch(resetCsat());
    }
  }

  onSubmit(e) {
    e.preventDefault();
    if (true) {
      this.setState(() => ({ error: "" }));
      this.props.dispatch(
        addCsat({
          userId: this.state.userId,
          comments: this.state.comments,
          contentId: this.state.contentId,
          rating: this.state.rating
        })
      );
    } else {
      this.setState(() => ({ errorMessage: "Please fix Validation Errors" }));
    }
  }

  render() {
    const { validated } = this.state;
    const { csat } = this.props;
    const { message, errorMessage } = this.props.Csats;
    // const { dispatch } = this.props;

    return (
      <PopUpAlert>
        {message ? (
          <h3>{message}</h3>
        ) : (
          <div>
            {errorMessage && (
              <div className="alert-danger" title="Errors...">
                {errorMessage}
              </div>
            )}
            <div
              style={{
                marginLeft: 10
              }}
            >
              How was your experience on this page?
            </div>
            <Form
              validate
              onSubmit={this.onSubmit}
              style={{
                marginLeft: 10
              }}
            >
              <fieldset>
                <Form.Group as={Row}>
                  <Form.Check
                    custom
                    inline
                    label="great"
                    type="radio"
                    id={`inline-radio-4`}
                    onClick={this.onrating4}
                  />
                  <Form.Check
                    custom
                    inline
                    label="good"
                    type="radio"
                    id={`inline-radio-3`}
                    onClick={this.onrating3}
                  />
                  <Form.Check
                    custom
                    inline
                    label="ok"
                    type="radio"
                    id={`inline-radio-2`}
                    onClick={this.onrating2}
                  />
                  <Form.Check
                    custom
                    inline
                    label="poor"
                    type="radio"
                    id={`inline-radio-1`}
                    onClick={this.onrating1}
                  />
                  <Form.Check
                    custom
                    inline
                    label="bad"
                    type="radio"
                    id={`inline-radio-0`}
                    onClick={this.onrating0}
                  />
                </Form.Group>
              </fieldset>

              <Form.Group controlId="exampleForm">
                <Form.Control
                  as="textarea"
                  rows="3"
                  placeholder="Please tell us what you love, or what we can do better."
                  defaultvalue={
                    typeof csat !== "undefined" ? csat.comments : null
                  }
                  // value=
                  onChange={this.oncommentsChange}
                />
              </Form.Group>

              <Button
                className={"mr-auto"}
                onClick={this.onSubmit}
                type="submit"
                disabled={validated && errorMessage === ""}
              >
                <span>Submit Your Feedback</span>
              </Button>
            </Form>
          </div>
        )}
      </PopUpAlert>
    );
  }
}

Csat.propTypes = {
  id: PropTypes.long,
  userId: PropTypes.long,
  comments: PropTypes.string,
  contentId: PropTypes.long,
  rating: PropTypes.float
};

function mapStateToProps(state) {
  let cs = {};
  if (state.Csats) {
    cs = state.Csats;
  }
  return {
    Csats: cs
  };
}

export default connect(mapStateToProps)(CsatQuestionnaire);
