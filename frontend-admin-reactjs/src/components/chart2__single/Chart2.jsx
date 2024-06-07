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

const Chart = ({ aspect, title, dataChuyenDi }) => {
  // Khởi tạo dữ liệu ban đầu với tất cả giá trị bằng 0
  const data = [
    {
      name: "Tháng 1",
      'Thành công': 0,
      'Bị hủy': 0,
    },
    {
      name: "Tháng 2",
      'Thành công': 0,
      'Bị hủy': 0,
    },
    {
      name: "Tháng 3",
      'Thành công': 0,
      'Bị hủy': 0,
    },
    {
      name: "Tháng 4",
      'Thành công': 0,
      'Bị hủy': 0,
    },
    {
      name: "Tháng 5",
      'Thành công': 0,
      'Bị hủy': 0,
    },
    {
      name: "Tháng 6",
      'Thành công': 0,
      'Bị hủy': 0,
    },
    {
      name: "Tháng 7",
      'Thành công': 0,
      'Bị hủy': 0,
    },
    {
      name: "Tháng 8",
      'Thành công': 0,
      'Bị hủy': 0,
    },
    {
      name: "Tháng 9",
      'Thành công': 0,
      'Bị hủy': 0,
    },
    {
      name: "Tháng 10",
      'Thành công': 0,
      'Bị hủy': 0,
    },
    {
      name: "Tháng 11",
      'Thành công': 0,
      'Bị hủy': 0,
    },
    {
      name: "Tháng 12",
      'Thành công': 0,
      'Bị hủy': 0,
    },
  ];


  if (dataChuyenDi && Object.keys(dataChuyenDi).length !== 0) {
    // Lấy số lượng giao dịch thành công và bị hủy của tháng 6
    const total_order_success_user_June = dataChuyenDi.total_order_success_user;
    const total_order_canceled_user_June = dataChuyenDi.total_order_canceled_user;

    // Cập nhật dữ liệu của tháng 6 trong biểu đồ
    const dataIndex = 5; // Tháng 6 ở index 5 trong mảng data
    data[dataIndex]['Thành công'] = total_order_success_user_June;
    data[dataIndex]['Bị hủy'] = total_order_canceled_user_June;
  }

  return (
    <div className="chart2">
      <div className="title-chart2">{title}</div>
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
            className="chatGrid-chart2"
          />
          <XAxis dataKey="name" />

          <Tooltip />
          <Legend />
          <Bar dataKey="Thành công" fill="#8884d8" />
          <Bar dataKey="Bị hủy" fill="#82ca9d" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
};

export default Chart;
