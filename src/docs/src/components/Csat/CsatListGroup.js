/**
 * Csat Data Lookup / ListGroup
 * 
 * 
 * 
 * 
 * 
 *
*/
import React from 'react';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';
import { ListGroup, Nav } from 'react-bootstrap';
import { listCsat, removeCsat } from '../../actions/Csats'

import Csat from './Csat';
import CsatHeader from './CsatHeader';

const CsatListGroup = (props) => (

    <ListGroup>
        <tbody>
        {typeof(props.Csats.map) !== 'undefined' ? props.Csats.map(csat => {
        	const {id} = csat;
            return (
                <ListGroup.Item key={csat.id} id={csat.id}>
                    <Csat {...csat} />
                    &nbsp;:&nbsp;
                    <Link to={`/Csat/edit/${id}`}>edit</Link>
                </ListGroup.Item>
            );
        }): <tr><td>no results</td></tr> }
    </ListGroup>
    
);

const mapStateToProps = (state) => {
    return {
        Csats: state.Csats
    };
}

export default connect(mapStateToProps)(CsatListGroup);
