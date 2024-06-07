import React from "react";
import {
  AreaChart,
  Area,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from "recharts";

import "./chart3.scss";


const Chart3 = ({ aspect, title, orderCount, balanceCount }) => {

  const data = [
    {
      "name": "Tháng 1",
      "Thành công": 0,
      "Bị hủy": 0,
    },
    {
      "name": "Tháng 2",
      "Thành công": 0,
      "Bị hủy": 0,
    },
    {
      "name": "Tháng 3",
      "Thành công": 0,
      "Bị hủy": 0,
    },
    {
      "name": "Tháng 4",
      "Thành công": 0,
      "Bị hủy": 0,
    },
    {
      "name": "Tháng 5",
      "Thành công": 0,
      "Bị hủy": 0,
    },
    {
      "name": "Tháng 6",
      "Thành công": orderCount,
      "Bị hủy": balanceCount,
    },
    {
      "name": "Tháng 7",
      "Thành công": 0,
      "Bị hủy": 0,
    },
    {
      "name": "Tháng 8",
      "Thành công": 0,
      "Bị hủy": 0,
    },
    {
      "name": "Tháng 9",
      "Thành công": 0,
      "Bị hủy": 0,
    },
    {
      "name": "Tháng 10",
      "Thành công": 0,
      "Bị hủy": 0,
    },
    {
      "name": "Tháng 11",
      "Thành công": 0,
      "Bị hủy": 0,
    },
    {
      "name": "Tháng 12",
      "Thành công": 0,
      "Bị hủy": 0,
    },

  ]

  return (
    <div className="chart3">
      <div className="title-chart3">{title}</div>
      <ResponsiveContainer width="100%" aspect={aspect}>
        <AreaChart width={730} height={250} data={data}
          margin={{ top: 10, right: 30, left: 0, bottom: 0 }}>
          <defs>
            <linearGradient id="colorUv" x1="0" y1="0" x2="0" y2="1">
              <stop offset="5%" stopColor="#8884d8" stopOpacity={0.8} />
              <stop offset="95%" stopColor="#8884d8" stopOpacity={0} />
            </linearGradient>
            <linearGradient id="colorPv" x1="0" y1="0" x2="0" y2="1">
              <stop offset="5%" stopColor="#82ca9d" stopOpacity={0.8} />
              <stop offset="95%" stopColor="#82ca9d" stopOpacity={0} />
            </linearGradient>
          </defs>
          <XAxis dataKey="name" />
          <YAxis />
          <CartesianGrid
            strokeDasharray="3 3"
            stroke="gray"
            className="chatGrid-chart3" />
          <Tooltip />
          <Area type="monotone" dataKey="Thành công" stroke="#8884d8" fillOpacity={1} fill="url(#colorUv)" />
          <Area type="monotone" dataKey="Bị hủy" stroke="#82ca9d" fillOpacity={1} fill="url(#colorPv)" />
        </AreaChart>
      </ResponsiveContainer>
    </div>
  );
};

export default Chart3;
