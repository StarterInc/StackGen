/**
 * Csat Data Object
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
 * 
 * or id: '',userId: '',contentId: '',comments: '',rating: '',
 * 
 */
import React from 'react';
import { connect } from 'react-redux';

const Csat = ({   id,
  userId,
  contentId,
  comments,
  rating,
 }) => (
    <>	
		<td>
			{ id }
		</td>
		<td>
			{ userId }
		</td>
		<td>
			{ contentId }
		</td>
		<td>
			{ comments }
		</td>
		<td>
			{ rating }
		</td>
    </>
);

export default connect()(Csat);
