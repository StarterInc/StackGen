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
 *  contentId: '',
 *  comments: '',
 *  rating: '',
 *  }
 */
import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Card, Form, Button } from 'react-bootstrap';

import { addCsat, editCsat, resetCsat } from '../../actions/Csats'
import Csat from './Csat';
import AlertDismissable from '../AlertDismissable';

class CsatForm extends React.Component {

	constructor(props) {
        super(props);

        this.onidChange = this.onidChange.bind(this);
        this.onuserIdChange = this.onuserIdChange.bind(this);
        this.oncontentIdChange = this.oncontentIdChange.bind(this);
        this.oncommentsChange = this.oncommentsChange.bind(this);
        this.onratingChange = this.onratingChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
        this.state = {
            id: props.Csat 
            	? props.Csat.id : '',
            userId: props.Csat 
            	? props.Csat.userId : '',
            contentId: props.Csat 
            	? props.Csat.contentId : '',
            comments: props.Csat 
            	? props.Csat.comments : '',
            rating: props.Csat 
            	? props.Csat.rating : '',
            error: '',
            validated: true
        };
    }
  
  /**
	 * Csat 
	 * Data Action Event Listener
	 * 
	 */
    onidChange(e) {
        const idval = e.target.value;
        this.setState(() => ({ id: idval }));
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
    oncontentIdChange(e) {
        const contentIdval = e.target.value;
        this.setState(() => ({ contentId: contentIdval }));
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
    onratingChange(e) {
        const ratingval = e.target.value;
        this.setState(() => ({ rating: ratingval }));
    }

  componentDidUpdate(previousProps, previousState){
    if(previousProps.submitting 
      && !this.props.errorMessage){
        this.props.dispatch(resetCsat())
    }
  }

    onSubmit(e) {
        e.preventDefault();
      if(this.state.validated) {

        this.setState(() => ({ error: '' }));
        this.props.onSubmitCsat({
                id: this.state.id,
                userId: this.state.userId,
                contentId: this.state.contentId,
                comments: this.state.comments,
                rating: this.state.rating,
          });
      }else{
            this.setState(() => ({errorMessage: "Please fix Validation Errors" }))
        }
    }

    render() {
        const { validated, message, errorMessage }  = this.state;
        const { csat } = this.props
        const { dispatch } = this.props;

        return (
          <>
          	<Card>
            <Card.Body>

              {message && <AlertDismissable className="alert-success" title="Success" >{message}</AlertDismissable>}
              {errorMessage && <AlertDismissable show={(errorMessage !== '')} className="alert-danger" title="Error"> <button type="button" className="close" data-dismiss="alert" aria-label="Close" onClick={() => dispatch(resetCsat())}>
              
              {message && (
                      <AlertDismissable className="alert-success" title="Success">
                        {message}
                      </AlertDismissable>
                    )}
                    {errorMessage && (
                      <AlertDismissable
                        show={errorMessage !== ""}
                        className="alert-danger"
                        title="Error"
                      >
                        {" "}
                        <button
                          type="button"
                          className="close"
                          data-dismiss="alert"
                          aria-label="Close"
                          onClick={() => dispatch(resetCsat())}
                        >
                          <span aria-hidden="true">&times;</span>
                        </button>
                        {errorMessage}
                      </AlertDismissable>
                    )}
              
              
              
              
              
              
              <span aria-hidden="true">&times;</span></button>{errorMessage}</AlertDismissable>}

                <Form
	                validate
	                validated={validated}
	                form onSubmit={this.onSubmit}
                >
                 <Form.Group
                  controlId="validationCustom02"
	              >
                  <Form.Label>id</Form.Label>
                  <Form.Control
                    false
                    type="text"
                    placeholder=""
                    defaultvalue=""
                    value={( typeof(csat) !== 'undefined' ? csat.id : null ) }
                    onChange={this.onidChange}
                  />
                  <Form.Control.Feedback>
                    NICE WORK!
                  </Form.Control.Feedback>
                  <Form.Control.Feedback type="invalid">
                    Please enter a valid id i.e.: 
                  </Form.Control.Feedback>
			          </Form.Group>
			     
                 <Form.Group
                  controlId="validationCustom02"
	              >
                  <Form.Label>userId</Form.Label>
                  <Form.Control
                    false
                    type="text"
                    placeholder=""
                    defaultvalue=""
                    value={( typeof(csat) !== 'undefined' ? csat.userId : null ) }
                    onChange={this.onuserIdChange}
                  />
                  <Form.Control.Feedback>
                    NICE WORK!
                  </Form.Control.Feedback>
                  <Form.Control.Feedback type="invalid">
                    Please enter a valid userId i.e.: 
                  </Form.Control.Feedback>
			          </Form.Group>
			     
                 <Form.Group
                  controlId="validationCustom02"
	              >
                  <Form.Label>contentId</Form.Label>
                  <Form.Control
                    true
                    type="text"
                    placeholder=""
                    defaultvalue=""
                    value={( typeof(csat) !== 'undefined' ? csat.contentId : null ) }
                    onChange={this.oncontentIdChange}
                  />
                  <Form.Control.Feedback>
                    NICE WORK!
                  </Form.Control.Feedback>
                  <Form.Control.Feedback type="invalid">
                    Please enter a valid contentId i.e.: 
                  </Form.Control.Feedback>
			          </Form.Group>
			     
                 <Form.Group
                  controlId="validationCustom02"
	              >
                  <Form.Label>comments</Form.Label>
                  <Form.Control
                    false
                    type="text"
                    placeholder="Your real feelings"
                    defaultvalue="Your real feelings"
                    value={( typeof(csat) !== 'undefined' ? csat.comments : null ) }
                    onChange={this.oncommentsChange}
                  />
                  <Form.Control.Feedback>
                    NICE WORK!
                  </Form.Control.Feedback>
                  <Form.Control.Feedback type="invalid">
                    Please enter a valid comments i.e.: Your real feelings
                  </Form.Control.Feedback>
			          </Form.Group>
			     
                 <Form.Group
                  controlId="validationCustom02"
	              >
                  <Form.Label>rating</Form.Label>
                  <Form.Control
                    false
                    type="text"
                    placeholder=""
                    defaultvalue=""
                    value={( typeof(csat) !== 'undefined' ? csat.rating : null ) }
                    onChange={this.onratingChange}
                  />
                  <Form.Control.Feedback>
                    NICE WORK!
                  </Form.Control.Feedback>
                  <Form.Control.Feedback type="invalid">
                    Please enter a valid rating i.e.: 
                  </Form.Control.Feedback>
			          </Form.Group>
			     

                <Button type="submit"
                    disabled={(validated &&
                        errorMessage === '')
                            }>
                    <span>{( typeof(csat) !== 'undefined' ? 'Edit' : 'Add' )} Csat</span>
                </Button>

              </Form>
            </Card.Body>
            </Card>
            </>
          
        );
    }
}


Csat.propTypes = {
		id : PropTypes.long,
		userId : PropTypes.long,
		contentId : PropTypes.long,
		comments : PropTypes.string,
		rating : PropTypes.long,
		};

function mapStateToProps(state) {
    return { Csats: state.csats }
}

//function mapDispatchToProps(dispatch) {
  //  return bindActionCreators({ addCsat }, dispatch)
//}

// {( typeof(csat) !== 'undefined' ? editCsat : addCsat )}

export default connect(mapStateToProps)(CsatForm);
