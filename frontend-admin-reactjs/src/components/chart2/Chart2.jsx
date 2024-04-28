import React from "react";
import {
  BarChart,
  Bar,
  XAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";

import "./chart2.scss";

const data = [
  {
    name: "Tháng 1",
    Mua: 3,
    Bán: 6,
  },
  {
    name: "Tháng 2",
    Mua: 6,
    Bán: 2,
  },
  {
    name: "Tháng 1",
    Mua: 4,
    Bán: 1,
  },
  {
    name: "Tháng 3",
    Mua: 1,
    Bán: 4,
  },
  {
    name: "Tháng 4",
    Mua: 1,
    Bán: 2,
  },
  {
    name: "Tháng 5",
    Mua: 9,
    Bán: 0,
  },
  {
    name: "Tháng 6",
    Mua: 2,
    Bán: 1,
  },

];
const Chart = ({ aspect, title }) => {
  return (
    <div className="chart">
      <div className="title">{title}</div>
      <ResponsiveContainer width="100%" aspect={aspect}>
        <BarChart
          width={730}
          height={250}
          margin={{ top: 0, right: 0, left: 0, bottom: 0 }}
          data={data}
        >
          <CartesianGrid
            strokeDasharray="3 3"
            stroke="gray"
            className="chatGrid"
          />
          <XAxis dataKey="name" />

          <Tooltip />
          <Legend />
          <Bar dataKey="Mua" fill="#8884d8" />
          <Bar dataKey="Bán" fill="#82ca9d" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
};

export default Chart;
