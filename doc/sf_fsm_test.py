# encoding=utf-8
# 当前版本 2024-06-04 16:30

import datetime
import random
import time
import unittest
from unittest import TestSuite

import requests
import os
import json

from path import Path

CODE_SUCC = 20000

PREFIX = "http://127.0.0.1:8080"
if "IN_JUDGE" in os.environ:
    PREFIX = "http://sf-fsm-server:8080"
# PREFIX = "http://227.0.0.1:8080"
ROOT_URL = PREFIX + "/"
PING_URL = PREFIX + "/ping"
RESET_URL = PREFIX + "/reset"
APP_CREATE_URL = PREFIX + "/api/app/create"
CREATE_URL = PREFIX + "/api/workflow/create"
QUERY_URL = PREFIX + "/api/workflow/query"
UPDATE_URL = PREFIX + "/api/workflow/update"
DELETE_URL = PREFIX + "/api/workflow/delete"
ACTION_URL = PREFIX + "/api/workflow/action"

PENDING = "PENDING"
COLLECTED = "COLLECTED"
PAYING = "PAYING"
PAID = "PAID"
TRANSITING = "TRANSITING"
DELIVERY = "DELIVERY"
DELIVERED = "DELIVERED"
REFUSED = "REFUSED"
EXCEPTION = "EXCEPTION"
CANCELLED = "CANCELLED"
COMPLETED = "COMPLETED"

COLLECT = "COLLECT"
PAY = "PAY"
SHIP = "SHIP"
CANCEL = "CANCEL"
AUTO = "AUTO"
# DELIVERY = "DELIVERY"

POSTMAN = "POSTMAN"
SENDER = "SENDER"
TRANSITER = "TRANSITER"
DRIVER = "DRIVER"
RECIPIENT = "RECIPIENT"

baseAppId = 0


def getBaseReq1(name):
    return {
        "appId": baseAppId,
        "name": name,
        "desc": name,
        "states": [
            {"code": PENDING, "name": "待揽收"},
            {"code": COLLECTED, "name": "已揽收"},
            {"code": PAYING, "name": "待支付"},
            {"code": PAID, "name": "已支付"},
            {"code": TRANSITING, "name": "运输中"},
            {"code": DELIVERY, "name": "派送中"},
            {"code": DELIVERED, "name": "已签收"},
            {"code": COMPLETED, "name": "已完成"},
            {"code": CANCELLED, "name": "已取消"},
            {"code": REFUSED, "name": "已拒收"},
            {"code": EXCEPTION, "name": "异常件"},
        ],
        "events": [
            {"name": COLLECT, "fromState": PENDING, "toState": COLLECTED, "role": POSTMAN},
            {"name": AUTO, "fromState": COLLECTED, "toState": PAYING, "role": AUTO},
            {"name": PAY, "fromState": PAYING, "toState": PAID, "role": SENDER},
            {"name": SHIP, "fromState": PAID, "toState": TRANSITING, "role": TRANSITER},
            {"name": DELIVERY, "fromState": TRANSITING, "toState": DELIVERY, "role": POSTMAN},
            {"name": DELIVERY, "fromState": DELIVERY, "toState": DELIVERED, "role": RECIPIENT},

            {"name": CANCEL, "fromState": PAYING, "toState": CANCELLED, "role": SENDER},
            {"name": CANCEL, "fromState": DELIVERY, "toState": REFUSED, "role": RECIPIENT},
            {"name": EXCEPTION, "fromState": DELIVERY, "toState": EXCEPTION, "role": POSTMAN},

            {"name": AUTO, "fromState": DELIVERED, "toState": COMPLETED, "role": AUTO},
            {"name": AUTO, "fromState": REFUSED, "toState": COMPLETED, "role": AUTO},
            {"name": AUTO, "fromState": CANCELLED, "toState": COMPLETED, "role": AUTO},
            {"name": AUTO, "fromState": EXCEPTION, "toState": COMPLETED, "role": AUTO},
        ]
    }


def getBaseReq2(name):
    return {
        "appId": baseAppId,
        "name": name,
        "desc": name,
        "states": [
            {"code": PENDING, "name": "待揽收"},
            {"code": COLLECTED, "name": "已揽收"},
            {"code": PAYING, "name": "待支付"},
            {"code": PAID, "name": "已支付"},
            {"code": TRANSITING, "name": "运输中"},
            {"code": DELIVERY, "name": "派送中"},
            {"code": DELIVERED, "name": "已签收"},
            {"code": COMPLETED, "name": "已完成"},
            {"code": CANCELLED, "name": "已取消"},
            {"code": REFUSED, "name": "已拒收"},
        ],
        "events": [
            {"name": COLLECT, "fromState": PENDING, "toState": COLLECTED, "role": POSTMAN},
            {"name": PAY, "fromState": PAYING, "toState": PAID, "role": SENDER},
            {"name": SHIP, "fromState": PAID, "toState": TRANSITING, "role": TRANSITER},
            {"name": DELIVERY, "fromState": TRANSITING, "toState": DELIVERY, "role": POSTMAN},
            {"name": DELIVERY, "fromState": DELIVERY, "toState": DELIVERED, "role": RECIPIENT},

            {"name": CANCEL, "fromState": PAYING, "toState": CANCELLED, "role": SENDER},
            {"name": CANCEL, "fromState": DELIVERY, "toState": REFUSED, "role": RECIPIENT},

            {"name": AUTO, "fromState": COLLECTED, "toState": PAYING, "role": AUTO},

            {"name": AUTO, "fromState": DELIVERED, "toState": COMPLETED, "role": AUTO},
            {"name": AUTO, "fromState": REFUSED, "toState": COMPLETED, "role": AUTO},
            {"name": AUTO, "fromState": CANCELLED, "toState": COMPLETED, "role": AUTO},
        ]
    }


def getBaseAppReq(name):
    return {
        "name": name,
        "desc": name,
        "states": [
            {"code": PENDING, "name": "待揽收"},
            {"code": COLLECTED, "name": "已揽收"},
            {"code": PAYING, "name": "待支付"},
            {"code": PAID, "name": "已支付"},
            {"code": TRANSITING, "name": "运输中"},
            {"code": DELIVERY, "name": "派送中"},
            {"code": DELIVERED, "name": "已签收"},
            {"code": COMPLETED, "name": "已完成"},
            {"code": CANCELLED, "name": "已取消"},
            {"code": REFUSED, "name": "已拒收"},
            {"code": EXCEPTION, "name": "异常件"},
        ],
        "beginState": "PENDING",
        "endState": "COMPLETED",
        "roles": [
            {
                "role": "POSTMAN",
                "auth": [
                    {"fromState": PENDING, "toState": COLLECTED},
                    {"fromState": TRANSITING, "toState": DELIVERY},
                    {"fromState": DELIVERY, "toState": EXCEPTION}
                ]
            },
            {
                "role": "SENDER",
                "auth": [
                    {"fromState": PAYING, "toState": PAID},
                    {"fromState": PENDING, "toState": CANCELLED},
                    {"fromState": PAYING, "toState": CANCELLED}
                ]
            },
            {
                "role": "TRANSITER",
                "auth": [
                    {"fromState": PAID, "toState": TRANSITING},
                ],
            },
            {
                "role": "DRIVER",
                "auth": [
                    {"fromState": PAID, "toState": TRANSITING},
                ],
            },
            {
                "role": "RECIPIENT",
                "auth": [
                    {"fromState": DELIVERY, "toState": DELIVERED},
                    {"fromState": DELIVERY, "toState": REFUSED},
                ],
            },
            {
                "role": "AUTO",
                "auth": [
                    {"fromState": DELIVERED, "toState": COMPLETED},
                    {"fromState": CANCELLED, "toState": COMPLETED},
                    {"fromState": REFUSED, "toState": COMPLETED},
                    {"fromState": EXCEPTION, "toState": COMPLETED},
                    {"fromState": COLLECTED, "toState": PAYING},
                ],
            }
        ]
    }


def getAppReq1(name):
    return {
        "name": name,
        "desc": name,
        "states": [
            {"code": "S_0", "name": "开始"},
            {"code": "S_1", "name": "中转"},
            {"code": "S_2", "name": "结束"},
        ],
        "beginState": "S_0",
        "endState": "S_2",
        "roles": [
            {
                "role": "ROLE_1",
                "auth": [
                    {"fromState": "S_0", "toState": "S_1"},
                    {"fromState": "S_1", "toState": "S_2"},
                    {"fromState": "S_0", "toState": "S_2"},
                ],
            }
        ]
    }


class TestAppCreate(unittest.TestCase):
    def testAppSimple(self):
        req = getAppReq1("testSimple")
        rsp = requests.post(APP_CREATE_URL, timeout=0.5, json=req).json()
        self.assertEqual(rsp["state"]["code"], CODE_SUCC, rsp)
        self.assertTrue(rsp["data"]["id"] > 0)

    def testAppStatesCntLt3(self):
        req = {
            "name": "testAppStatesCntLt3",
            "desc": "testAppStatesCntLt3",
            "states": [
                {"code": "S_0", "name": "开始"},
                {"code": "S_1", "name": "结束"},
            ],
            "beginState": "S_0",
            "endState": "S_1",
            "roles": [
                {
                    "role": "ROLE_1",
                    "auth": [
                        {"fromState": "S_0", "toState": "S_1"},
                    ],
                }
            ]
        }
        rsp = requests.post(APP_CREATE_URL, timeout=0.5, json=req).json()
        self.assertNotEqual(rsp["state"]["code"], CODE_SUCC)

    def testErrorBeginEndState(self):
        req = getAppReq1("app_error_begin_state")
        req["beginState"] = "S_3"
        rsp = requests.post(APP_CREATE_URL, timeout=0.5, json=req).json()
        self.assertNotEqual(rsp["state"]["code"], CODE_SUCC)
        # self.assertTrue(rsp["data"]["id"] > 0)

        req = getAppReq1("app_error_end_state")
        req["endState"] = "S_3"
        rsp = requests.post(APP_CREATE_URL, timeout=0.5, json=req).json()
        self.assertNotEqual(rsp["state"]["code"], CODE_SUCC)
        # self.assertTrue(rsp["data"]["id"] > 0)

    def testErrorRoleAuth(self):
        req = getAppReq1("app_error_role_state1")
        req["roles"][0]["auth"].append({"fromState": "S_0", "toState": "S_0"})
        rsp = requests.post(APP_CREATE_URL, timeout=0.5, json=req).json()
        self.assertNotEqual(rsp["state"]["code"], CODE_SUCC)

        req = getAppReq1("app_error_role_state2")
        req["roles"][0]["auth"].append({"fromState": "S_2", "toState": "S_1"})
        rsp = requests.post(APP_CREATE_URL, timeout=0.5, json=req).json()
        self.assertNotEqual(rsp["state"]["code"], CODE_SUCC)

        req = getAppReq1("app_error_role_state3")
        req["roles"][0]["auth"].append({"fromState": "S_1", "toState": "S_0"})
        rsp = requests.post(APP_CREATE_URL, timeout=0.5, json=req).json()
        self.assertNotEqual(rsp["state"]["code"], CODE_SUCC)


class TestSimple(unittest.TestCase):
    def testCreateCycleError(self):
        req = {
            "name": "appC",
            "desc": "appC",
            "states": [
                {"code": "S_0", "name": "开始"},
                {"code": "S_1", "name": "中转1"},
                {"code": "S_2", "name": "中转2"},
                {"code": "S_3", "name": "中转2"},
                {"code": "S_4", "name": "结束"},
            ],
            "beginState": "S_0",
            "endState": "S_4",
            "roles": [
                {
                    "role": "ROLE_1",
                    "auth": [
                        {"fromState": "S_0", "toState": "S_1"},
                        {"fromState": "S_1", "toState": "S_2"},
                        {"fromState": "S_2", "toState": "S_3"},
                        {"fromState": "S_3", "toState": "S_4"},
                        {"fromState": "S_3", "toState": "S_1"},
                    ],
                }
            ]
        }
        rsp = requests.post(APP_CREATE_URL, timeout=0.5, json=req).json()
        self.assertEqual(rsp["state"]["code"], CODE_SUCC, rsp)
        self.assertTrue(rsp["data"]["id"] > 0, rsp)
        appId = rsp["data"]["id"]
        req = {
            "appId": appId,
            "name": "appE",
            "desc": "appE",
            "states": [
                {"code": "S_0", "name": "开始"},
                {"code": "S_1", "name": "中转1"},
                {"code": "S_2", "name": "中转2"},
                {"code": "S_3", "name": "中转2"},
                {"code": "S_4", "name": "结束"},
            ],
            "events": [
                {"name": "E_1", "fromState": "S_0", "toState": "S_1", "role": "ROLE_1"},
                {"name": "E_2", "fromState": "S_1", "toState": "S_2", "role": "ROLE_1"},
                {"name": "E_3", "fromState": "S_2", "toState": "S_3", "role": "ROLE_1"},
                {"name": "E_4", "fromState": "S_3", "toState": "S_4", "role": "ROLE_1"},
                {"name": "E_5", "fromState": "S_3", "toState": "S_1", "role": "ROLE_1"},
            ]
        }
        rsp = requests.post(CREATE_URL, timeout=0.5, json=req).json()
        self.assertNotEqual(rsp["state"]["code"], CODE_SUCC, rsp)


# 流程创建
class TestCreate(unittest.TestCase):

    def setUp(self):
        req = getBaseAppReq("base_app")
        rsp = requests.post(APP_CREATE_URL, timeout=0.5, json=req).json()
        self.assertEqual(rsp["state"]["code"], CODE_SUCC, rsp)
        self.assertTrue(rsp["data"]["id"] > 0, rsp)
        global baseAppId
        baseAppId = rsp["data"]["id"]

    def test_create_full(self):
        rsp = requests.post(CREATE_URL, json=getBaseReq1("create_full")).json()
        self.assertEqual(rsp["state"]["code"], CODE_SUCC, rsp)
        self.assertTrue(rsp["data"]["id"] > 0, rsp)

        rsp = requests.post(CREATE_URL, json=getBaseReq2("create_2")).json()
        self.assertEqual(rsp["state"]["code"], CODE_SUCC, rsp)
        self.assertTrue(rsp["data"]["id"] > 0, rsp)

    # 状态转换逻辑错误： 尝试创建一个流程，其中包含逻辑上不可能的状态转换（如从“已签收”直接转换到“运输中”），验证系统是否能识别并阻止这种不合理的流程创建。
    def test_create_state_error(self):
        req = {
            "appId": baseAppId,
            "name": "test2",
            "states": [
                {"code": PENDING, "name": "待揽收"},
                {"code": COLLECTED, "name": "已揽收"},
                {"code": PAYING, "name": "待支付"},
                {"code": PAID, "name": "已支付"},
                {"code": TRANSITING, "name": "运输中"},
                {"code": DELIVERY, "name": "派送中"},
                {"code": DELIVERED, "name": "已签收"},
                {"code": COMPLETED, "name": "已完成"}
            ],
            "events": [
                {"name": "揽收", "fromState": PENDING, "toState": COLLECTED, "role": POSTMAN},
                {"name": "待支付", "fromState": COLLECTED, "toState": PAYING, "role": AUTO},
                {"name": "已支付", "fromState": PAYING, "toState": PAID, "role": SENDER},
                {"name": "运输", "fromState": PAID, "toState": TRANSITING, "role": TRANSITER},
                {"name": "派送", "fromState": TRANSITING, "toState": DELIVERY, "role": DRIVER},
                {"name": "签收", "fromState": DELIVERY, "toState": DELIVERED, "role": RECIPIENT},
                {"name": "完成", "fromState": DELIVERED, "toState": COMPLETED, "role": AUTO},
                {"name": "运输", "fromState": COMPLETED, "toState": DELIVERED, "role": AUTO}
            ]
        }
        rsp = requests.post(CREATE_URL, timeout=0.5, json=req).json()
        self.assertNotEqual(rsp["state"]["code"], CODE_SUCC)
        self.assertTrue("data" not in rsp or "id" not in rsp["data"])

    # 缺少必要状态： 尝试创建一个流程，故意省略一个或多个必要的状态（如“已支付”），以验证系统是否能检测到缺少的状态并提示错误。
    def test_create_state_lack(self):
        req = getBaseReq1("fail_state_lack")
        req["states"].pop(3)
        rsp = requests.post(CREATE_URL, timeout=0.5, json=req).json()
        self.assertNotEqual(rsp["state"]["code"], CODE_SUCC, rsp)
        self.assertTrue("data" not in rsp or "id" not in rsp["data"], rsp)

    # 状态命名重复或⾮法： 尝试创建⼀个流程，使⽤重复或⾮法的状态名称 （如使⽤空字符串或特殊字符作为状态名），验 证系统是否能正确处理这种异常输⼊。
    def test_create_name_error_1(self):
        req = getBaseReq1("fail_name_error_1")
        req["name"] = ""
        rsp = requests.post(CREATE_URL, timeout=0.5, json=req).json()
        self.assertNotEqual(rsp["state"]["code"], CODE_SUCC, "empty name")
        self.assertTrue("data" not in rsp or "id" not in rsp["data"])

        req = getBaseReq1("fail_name_duplicate_5")
        rsp = requests.post(CREATE_URL, timeout=0.5, json=req).json()
        self.assertEqual(rsp["state"]["code"], CODE_SUCC, rsp)
        rsp = requests.post(CREATE_URL, timeout=0.5, json=req).json()
        self.assertNotEqual(rsp["state"]["code"], CODE_SUCC, "duplicate name")
        self.assertTrue("data" not in rsp or "id" not in rsp["data"])

    def test_update_name_error2(self):
        req = getBaseReq1("fail_name_error_2_1")
        rsp = requests.post(CREATE_URL, timeout=0.5, json=req).json()
        self.assertEqual(rsp["state"]["code"], CODE_SUCC)
        req = getBaseReq1("fail_name_error_2_2")
        rsp = requests.post(CREATE_URL, timeout=0.5, json=req).json()
        self.assertEqual(rsp["state"]["code"], CODE_SUCC)
        wId = rsp["data"]["id"]
        req = getBaseReq1("fail_name_error_2_1")
        req["id"] = wId
        req.pop("appId")
        rsp = requests.post(UPDATE_URL, timeout=0.5, json=req).json()
        self.assertNotEqual(rsp["state"]["code"], CODE_SUCC)

    def test_create_fail_isolated_error_1(self):
        for i in range(20):
            req = getBaseReq1(f"fail_isolated_error_1_{i}")
            req["states"].pop(random.randint(0, len(req["states"]) - 1))
            rsp = requests.post(CREATE_URL, timeout=0.5, json=req).json()
            self.assertNotEqual(rsp["state"]["code"], CODE_SUCC)
            self.assertTrue("data" not in rsp or "id" not in rsp["data"])


# 流程查询
class TestQuery(unittest.TestCase):

    def test_query_all(self):
        for i in range(10):
            req = getBaseReq1("z_test_query_%04d" % i)
            rsp = requests.post(CREATE_URL, timeout=0.5, json=req).json()
            self.assertEqual(rsp["state"]["code"], CODE_SUCC)
            self.assertTrue(rsp["data"]["id"] > 0)
        fsmList = []
        pageCnt = 0
        while True:
            pageCnt += 1
            req = {
                "page": pageCnt,
                "pageSize": 10,
            }
            rsp = requests.post(QUERY_URL, timeout=0.5, json=req).json()
            # print(f"page: {pageCnt}, totalPage: {rsp['data']['totalPage']}")
            self.assertTrue(pageCnt < 100)
            self.assertEqual(rsp["data"]["size"], req["pageSize"])
            self.assertTrue(rsp["data"]["size"] == len(rsp["data"]["content"]) or pageCnt == rsp["data"]["totalPage"])
            # self.assertEqual(rsp["data"]["size"], len(rsp["data"]["content"]))
            fsmList.extend(rsp["data"]["content"])
            if pageCnt == rsp["data"]["totalPage"]:
                break

        idList = [item["id"] for item in fsmList]
        timeList = [item["createdTime"] for item in fsmList]
        self.assertEqual(len(idList), len(set(idList)))
        self.assertEqual(idList, sorted(idList, reverse=True))
        self.assertEqual(timeList, sorted(timeList, reverse=True))
        self.assertEqual(len(idList), rsp["data"]["total"])

    def test_query_default_param(self):
        req = {}
        rsp = requests.post(QUERY_URL, timeout=0.5, json=req).json()
        self.assertEqual(rsp["state"]["code"], CODE_SUCC)
        self.assertEqual(rsp["data"]["size"], 20)
        self.assertEqual(rsp["data"]["page"], 1)
        req2 = {"page": 1, "pageSize": 20, }
        rsp2 = requests.post(QUERY_URL, timeout=0.5, json=req2).json()
        self.assertEqual(rsp, rsp2)

    # 正常流: ⻚码指定⽆效的情况下（例如⻚码是-1），能查询出第⼀⻚的流程列表，排序应该是按照创建时间倒序
    def test_page_error(self):
        req1 = {"page": -3, "pageSize": 10, }
        rsp1 = requests.post(QUERY_URL, timeout=0.5, json=req1).json()
        req2 = {"page": 1, "pageSize": 10, }
        rsp2 = requests.post(QUERY_URL, timeout=0.5, json=req1).json()
        self.assertEqual(rsp1, rsp2)


"""
    流程删除
"""


class TestDelete(unittest.TestCase):

    # 指定⼀个存在的流程ID，能正常删除改流程；
    # 指定⼀个不存在的流程ID，能正常提示出错误
    def test_delete_normal(self):
        for i in range(10):
            req = getBaseReq1("z_test_delete_%04d" % i)
            rsp = requests.post(CREATE_URL, timeout=0.5, json=req).json()
            self.assertEqual(rsp["state"]["code"], CODE_SUCC)
            self.assertTrue(rsp["data"]["id"] > 0)
        oldFsmList = []
        pageCnt = 0
        while True:
            pageCnt += 1
            req = {
                "page": pageCnt,
                "pageSize": 20,
            }
            rsp = requests.post(QUERY_URL, timeout=0.5, json=req).json()
            self.assertTrue(pageCnt < 100)
            self.assertTrue(rsp["data"]["size"] <= req["pageSize"])
            oldFsmList.extend(rsp["data"]["content"])
            if pageCnt == rsp["data"]["totalPage"]:
                break

        oldIdList = [item["id"] for item in oldFsmList]
        deleteId = oldIdList[random.randint(0, len(oldIdList) - 1)]

        rsp = requests.post(DELETE_URL, timeout=0.5, json={"id": deleteId}).json()
        # print(f"oldIdList: {oldIdList}, deleteId: {deleteId}, rsp: {rsp}")
        self.assertEqual(rsp["state"]["code"], CODE_SUCC)

        newFsmList = []
        pageCnt = 0
        while True:
            pageCnt += 1
            req = {
                "page": pageCnt,
                "pageSize": 10,
            }
            rsp = requests.post(QUERY_URL, timeout=0.5, json=req).json()
            self.assertEqual(rsp["state"]["code"], CODE_SUCC)
            self.assertTrue(pageCnt < 100)
            self.assertTrue(rsp["data"]["size"] <= req["pageSize"])
            newFsmList.extend(rsp["data"]["content"])
            if pageCnt == rsp["data"]["totalPage"]:
                break
        newIdList = [item["id"] for item in newFsmList]
        self.assertTrue(deleteId not in newIdList)
        newIdList.append(deleteId)
        self.assertEqual(set(newIdList), set(oldIdList))

        # 测试已经被删除的 id 就行
        for did in [deleteId, -1, 999999]:
            rsp = requests.post(DELETE_URL, timeout=0.5, json={"id": deleteId}).json()
            self.assertNotEqual(rsp["state"]["code"], CODE_SUCC)

    # TODO: 指定⼀个存在的流程ID，但是该流程正在进⾏中，能正常提示错误, 考虑在 TestExecute 中来实现？
    def test_on_process(self):
        req = getBaseReq1("test_delete_on_process")
        rsp = requests.post(CREATE_URL, timeout=0.5, json=req).json()
        self.assertEqual(rsp["state"]["code"], CODE_SUCC)
        self.assertTrue(rsp["data"]["id"] > 0)
        wId = rsp["data"]["id"]
        rsp = requests.post(ACTION_URL, timeout=0.5, json={"id": wId, "action": "COLLECT", "role": "POSTMAN"}).json()
        self.assertEqual(rsp["state"]["code"], CODE_SUCC)
        rsp = requests.post(DELETE_URL, timeout=0.5, json={"id": wId}).json()
        self.assertNotEqual(rsp["state"]["code"], CODE_SUCC)

    def test_on_update_fail(self):
        req = getBaseReq1("test_update_on_process")
        rsp = requests.post(CREATE_URL, timeout=0.5, json=req).json()
        self.assertEqual(rsp["state"]["code"], CODE_SUCC, rsp)
        self.assertTrue(rsp["data"]["id"] > 0)
        wId = rsp["data"]["id"]

        req = getBaseReq2("test_update_on_process")
        req["id"] = wId
        rsp = requests.post(UPDATE_URL, timeout=0.5, json=req).json()
        self.assertEqual(rsp["state"]["code"], CODE_SUCC, rsp)

        rsp = requests.post(ACTION_URL, timeout=0.5, json={"id": wId, "action": "COLLECT", "role": "POSTMAN"}).json()
        self.assertEqual(rsp["state"]["code"], CODE_SUCC)

        req = getBaseReq1("test_update_on_process")
        req["id"] = wId
        rsp = requests.post(UPDATE_URL, timeout=0.5, json=req).json()
        self.assertNotEqual(rsp["state"]["code"], CODE_SUCC)


class TestAction(unittest.TestCase):
    def test_Action1(self):
        req = getBaseReq1("test-action-1-0")
        rsp = requests.post(CREATE_URL, timeout=0.5, json=req).json()
        self.assertEqual(rsp["state"]["code"], CODE_SUCC)
        self.assertTrue(rsp["data"]["id"] > 0)
        wId = rsp["data"]["id"]
        for (action, role) in [(COLLECT, POSTMAN), (PAY, SENDER), (SHIP, TRANSITER), (DELIVERY, POSTMAN),
                               (DELIVERY, RECIPIENT)]:
            rsp = requests.post(ACTION_URL, timeout=0.5, json={"id": wId, "action": action, "role": role}).json()
            self.assertEqual(rsp["state"]["code"], CODE_SUCC)
            rsp = requests.post(DELETE_URL, timeout=0.5, json={"id": wId}).json()
            self.assertNotEqual(rsp["state"]["code"], CODE_SUCC)

        req = getBaseReq1("test-action-1-1")
        rsp = requests.post(CREATE_URL, timeout=0.5, json=req).json()
        self.assertEqual(rsp["state"]["code"], CODE_SUCC)
        self.assertTrue(rsp["data"]["id"] > 0)
        wId = rsp["data"]["id"]
        for (action, role) in [(COLLECT, POSTMAN), (PAY, SENDER), (SHIP, TRANSITER), (DELIVERY, POSTMAN),
                               (EXCEPTION, POSTMAN)]:
            rsp = requests.post(ACTION_URL, timeout=0.5, json={"id": wId, "action": action, "role": role}).json()
            self.assertEqual(rsp["state"]["code"], CODE_SUCC)
            rsp = requests.post(DELETE_URL, timeout=0.5, json={"id": wId}).json()
            self.assertNotEqual(rsp["state"]["code"], CODE_SUCC)

        req = getBaseReq1("test-action-1-2")
        rsp = requests.post(CREATE_URL, timeout=0.5, json=req).json()
        self.assertEqual(rsp["state"]["code"], CODE_SUCC)
        self.assertTrue(rsp["data"]["id"] > 0)
        wId = rsp["data"]["id"]
        for (action, role, valid) in [(COLLECT, POSTMAN, True), (CANCEL, POSTMAN, False), (CANCEL, SENDER, True)]:
            rsp = requests.post(ACTION_URL, timeout=0.5, json={"id": wId, "action": action, "role": role}).json()
            if valid:
                self.assertEqual(rsp["state"]["code"], CODE_SUCC)
            else:
                self.assertNotEqual(rsp["state"]["code"], CODE_SUCC)

    def testAction2(self):
        req = getBaseReq1("test-action-2-0")
        rsp = requests.post(CREATE_URL, timeout=0.5, json=req).json()
        self.assertEqual(rsp["state"]["code"], CODE_SUCC)
        self.assertTrue(rsp["data"]["id"] > 0)
        wId = rsp["data"]["id"]
        for (action, role, valid) in [(COLLECT, POSTMAN, True), (PAY, POSTMAN, False), (PAY, SENDER, True),
                                      (SHIP, TRANSITER, True), (DELIVERY, POSTMAN, True),
                                      (EXCEPTION, POSTMAN, True)]:
            rsp = requests.post(ACTION_URL, timeout=0.5, json={"id": wId, "action": action, "role": role}).json()
            # print(f"action: {action}, role: {role}, valid: {valid}, rsp: {rsp}")
            if valid:
                self.assertEqual(rsp["state"]["code"], CODE_SUCC)
            else:
                self.assertNotEqual(rsp["state"]["code"], CODE_SUCC)

        req = getBaseReq2("test-action-2-1")
        rsp = requests.post(CREATE_URL, timeout=0.5, json=req).json()
        self.assertEqual(rsp["state"]["code"], CODE_SUCC)
        self.assertTrue(rsp["data"]["id"] > 0)
        wId = rsp["data"]["id"]
        for (action, role, valid) in [(COLLECT, POSTMAN, True), (CANCEL, POSTMAN, False), (CANCEL, SENDER, True)]:
            rsp = requests.post(ACTION_URL, timeout=0.5, json={"id": wId, "action": action, "role": role}).json()
            if valid:
                self.assertEqual(rsp["state"]["code"], CODE_SUCC)
            else:
                self.assertNotEqual(rsp["state"]["code"], CODE_SUCC)


def getCheckStyleScore():
    if "IN_JUDGE" not in os.environ:
        return 0
    lineCnt = 0
    with open("./javafiles.txt") as f:
        for line in f.readlines():
            lineCnt += 1
    if lineCnt == 0:
        return 0

    lineCnt = 0
    with open("./checkstyle.txt") as f:
        for line in f.readlines():
            lineCnt += 1
    return min(30, max(0, (102 - lineCnt) * 0.3))


if __name__ == '__main__':
    extras = {}
    judgeResult = {
        "code": 0,
        "score": 0,
        "extras": extras,
    }
    started = False
    for i in range(40):
        print(f"{datetime.datetime.now()}, 等待应用启动{i}")
        time.sleep(0.5)
        if started:
            break
        try:
            j = requests.get(ROOT_URL, timeout=0.5)
            if j.status_code is not None and j.status_code >= 100:
                started = True
        except Exception as e:
            print(f"检查应用启动失败-{i}: {e}")
            continue
    if not started:
        judgeResult["code"] = 3
        judgeResult["extras"]["hint"] = "应用启动失败"
        with open("./result.json", "w") as f:
            json.dump(judgeResult, f)
        exit(0)

    runner = unittest.TextTestRunner(verbosity=2)
    # result = unittest.main()
    # unittest.TestLoader().loadTestsFromTestCase()
    suite = TestSuite()
    suite.addTests(unittest.defaultTestLoader.loadTestsFromTestCase(TestAppCreate))
    suite.addTests(unittest.defaultTestLoader.loadTestsFromTestCase(TestSimple))
    suite.addTests(unittest.defaultTestLoader.loadTestsFromTestCase(TestCreate))
    suite.addTests(unittest.defaultTestLoader.loadTestsFromTestCase(TestDelete))
    suite.addTests(unittest.defaultTestLoader.loadTestsFromTestCase(TestQuery))
    suite.addTests(unittest.defaultTestLoader.loadTestsFromTestCase(TestAction))
    result = runner.run(suite)
    print(f"result: {result}")
    print(f"result.testsRun: {result.testsRun}")
    print(f"result.failures: {result.failures}")
    print(f"result.errors: {result.errors}")
    succCnt = result.testsRun - len(result.failures) - len(result.errors)
    # print(result.expectedFailures)
    # print(result.unexpectedSuccesses)
    judgeResult["code"] = 28
    checkStyleScore = getCheckStyleScore()
    if succCnt > 5:
        judgeResult["score"] = succCnt / result.testsRun * 70 + checkStyleScore
    else:
        judgeResult["score"] = succCnt / result.testsRun * 70

    judgeResult["extras"]["testsRun"] = result.testsRun
    judgeResult["extras"]["failuresCnt"] = len(result.failures)
    judgeResult["extras"]["errorCnt"] = len(result.errors)
    judgeResult["extras"]["failures"] = [x[0]._testMethodName for x in result.failures]
    judgeResult["extras"]["errors"] = [x[0]._testMethodName for x in result.errors]
    judgeResult["extras"]["unittestScore"] =  succCnt / result.testsRun * 70
    judgeResult["extras"]["checkStyleScore"] = checkStyleScore

    print(json.dumps(judgeResult))
    with open("./result.json", "w") as f:
        json.dump(judgeResult, f)
