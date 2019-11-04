/*
 * Edit Form Wrapper
 *
 * 
 * 
 * 
 * 
 *
 */
import React from 'react';
import CsatForm from './CsatForm';
import { connect } from 'react-redux';
import { Col, Row, Card } from 'react-bootstrap';

import Csat from './Csat';
import { editCsat } from '../../actions/Csats';

const EditCsat = (props) => (
    <Card>
        <Card.Header>Edit the Csat</Card.Header>
        <Card.Body>
        <CsatForm
            csat={props.csat}
            onSubmitCsat={(csat) => {
                props.dispatch(editCsat(props.csat.id,
                		csat));
                props.history.push('/');
            }}
        />
        </Card.Body>
    </Card>
);

/*
* locate this Csat instance in the state array of Csats,
* then return the form props with the data from the Csat instance.
*/
const mapStateToProps = (state, props) => {
    const { Csats } = state;
    const _id = parseInt(props.match.params.id);
    return { 
        csat:Csats.find( csat => csat.id === _id)
    };
}; 

export default connect(mapStateToProps)(EditCsat);
