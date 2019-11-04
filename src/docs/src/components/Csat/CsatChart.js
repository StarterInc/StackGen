import React from "react";
import { Bar } from "react-chartjs-2";

const data = {
  labels: [
	  "id","userId","contentId","comments","rating",
	  ],
  datasets: [
    {
      label: "Csat",
      backgroundColor: "rgba(255,99,132,0.2)",
      borderColor: "rgba(255,99,132,1)",
      borderWidth: 1,
      hoverBackgroundColor: "rgba(255,99,132,0.4)",
      hoverBorderColor: "rgba(255,99,132,1)",
      data: [65, 59, 80, 81, 56, 55, 40, 65, 59, 80, 81, 56, 55, 40]
    }
  ]
};

class CsatChart extends React.Component {
  render() {
    return (
      <Bar
        data={data}
        options={ { maintainAspectRatio: false } }
        width={600}
        height={250}
      />
    );
  }
}

export default Chart;
