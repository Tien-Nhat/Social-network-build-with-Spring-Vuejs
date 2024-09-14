package personalproject.social.network.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import personalproject.social.network.model.EReportStatus;
import personalproject.social.network.model.Report;
import personalproject.social.network.model.User;
import personalproject.social.network.repository.PostRepository;
import personalproject.social.network.repository.ReportRepository;
import personalproject.social.network.repository.ReportTypeRepository;
import personalproject.social.network.repository.UserRepository;
import personalproject.social.network.request.ReportRequest;
import personalproject.social.network.response.DataResponse;
import personalproject.social.network.response.ReportResponse;
import personalproject.social.network.util.Constant;
import personalproject.social.network.util.MessageCodes;
import personalproject.social.network.util.Utils;

@Service
public class ReportServiceImpl implements ReportService {

	private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

	@Autowired
	ReportRepository reportRepository;

	@Autowired
	ReportTypeRepository reportTypeRepository;

	@Autowired
	PostRepository postRepository;

	@Autowired
	AuthenticationFacade authenticationFacade;

	@Autowired
	Utils utils;

	@Autowired
	UserRepository userRepository;

	@Autowired
	Environment env;

	@Override
	public DataResponse getReportType() {
		DataResponse res = new DataResponse();
		try {
			res.setStatus(Constant.SUCCESS);
			res.setResult(reportTypeRepository.findAll());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return res;
	}

	@Override
	public DataResponse addReport(ReportRequest reportRequest) {
		DataResponse res = new DataResponse();
		try {
			Authentication authentication = authenticationFacade.getAuthentication();
			User user = userRepository.findByUsername(authentication.getName()).get();
			Report report = new Report();
			report.setPost(postRepository.findById(reportRequest.getPostId()).get());
			report.setUser(user);
			report.setReportDate(utils.getDateNow());
			report.setReason(reportRequest.getReason());
			report.setReportType(reportTypeRepository.findById(reportRequest.getReportTypeId()).get());
			reportRepository.save(report);

			res.setStatus(Constant.SUCCESS);
			res.setMessage(env.getProperty(MessageCodes.INFO_0034));
		} catch (Exception e) {
			System.out.print(e);
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0001));
		}
		return res;
	}

	@Override
	public DataResponse changeStatus(Long reportId, String status) {
		DataResponse res = new DataResponse();
		try {
			Report report = reportRepository.findById(reportId).get();
			switch (status) {
			case "Open":
				report.setStatus(EReportStatus.OPEN);
				break;
			case "In_progressing":
				report.setStatus(EReportStatus.IN_PROGRESSING);
				break;
			case "Resolved":
				report.setStatus(EReportStatus.RESOLVED);
				break;
			default:
				break;
			}

			reportRepository.save(report);
			res.setStatus(Constant.SUCCESS);
			res.setMessage(env.getProperty(MessageCodes.INFO_0035));
			res.setResult(report.getStatus());
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0001));
		}
		return res;
	}

	@Override
	public DataResponse getListReport() {
		DataResponse res = new DataResponse();
		try {
			List<ReportResponse> listReport = reportRepository.findAllReportResponses();
			res.setResult(listReport);
			res.setStatus(Constant.SUCCESS);
			res.setMessage(env.getProperty(MessageCodes.INFO_0036));
		} catch (Exception e) {
			res.setStatus(Constant.ERROR);
			res.setMessage(env.getProperty(MessageCodes.ERROR_0001));
			logger.error(e.getMessage());
		}
		return res;
	}

}
