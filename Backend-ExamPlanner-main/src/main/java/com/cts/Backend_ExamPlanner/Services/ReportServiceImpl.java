package com.cts.Backend_ExamPlanner.Services;

import com.cts.Backend_ExamPlanner.Services.ReportService;
import com.cts.Backend_ExamPlanner.dto.ReportDTO;
import com.cts.Backend_ExamPlanner.entities.Report;
import com.cts.Backend_ExamPlanner.repositories.ReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository repo;

    public ReportServiceImpl(ReportRepository repo) {
        this.repo = repo;
    }


    @Override
    public ReportDTO create(ReportDTO dto) {
        Report e = toEntity(dto);
        e.setId(null); // ensure insert
        Report saved = repo.save(e);
        return toDto(saved);
    }

    @Override
    public List<ReportDTO> saveBulk(List<ReportDTO> dtos) {
        List<Report> entities = dtos.stream()
                .map(this::toEntity)
                .peek(r -> r.setId(null))
                .collect(Collectors.toList());
        List<Report> saved = repo.saveAll(entities);
        return saved.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ReportDTO> findAll() {
        return repo.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public List<ReportDTO> getByStudent(String studentId) {
        return repo.findByStudentId(studentId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ReportDTO> getByExam(Long examId) {
        return repo.findByExamId(examId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public void deleteByStudent(String studentId) {
        repo.deleteByStudentId(studentId);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public Page<ReportDTO> searchPaged(int page,
                                       int size,
                                       String sort,
                                       String direction,
                                       String studentId,
                                       Long examId,
                                       LocalDateTime from,
                                       LocalDateTime to) {

        List<Report> all = repo.findAll();

        // 2) Apply filters in-memory
        List<Report> filtered = all.stream()
                .filter(r -> {
                    if (StringUtils.hasText(studentId)) {
                        String val = Optional.ofNullable(r.getStudentId()).orElse("");
                        if (!val.toLowerCase().contains(studentId.toLowerCase())) return false;
                    }
                    return true;
                })
                .filter(r -> examId == null || Objects.equals(r.getExamId(), examId))
                .filter(r -> {
                    if (from != null && to != null) {
                        LocalDateTime ts = r.getCreatedAt();
                        return ts != null && ( !ts.isBefore(from) && !ts.isAfter(to) );
                    }
                    return true;
                })
                .collect(Collectors.toList());


        String sortField = StringUtils.hasText(sort) ? sort : "createdAt";
        boolean asc = "ASC".equalsIgnoreCase(direction);

        Comparator<Report> comparator = comparatorFor(sortField, asc);
        filtered.sort(comparator);

        int total = filtered.size();
        int fromIndex = Math.min(page * size, total);
        int toIndex = Math.min(fromIndex + size, total);
        List<ReportDTO> pageContent = filtered.subList(fromIndex, toIndex)
                .stream().map(this::toDto).toList();

        return new PageImpl<>(pageContent, org.springframework.data.domain.PageRequest.of(page, size, asc ? Sort.by(sortField).ascending() : Sort.by(sortField).descending()), total);
    }

    private Comparator<Report> comparatorFor(String sortField, boolean asc) {
        Comparator<Report> c;

        switch (sortField) {
            case "id" -> c = Comparator.comparing(Report::getId, nullsLast());
            case "examId" -> c = Comparator.comparing(Report::getExamId, nullsLast());
            case "studentId" -> c = Comparator.comparing(Report::getStudentId, nullsLast());
            case "reportName" -> c = Comparator.comparing(Report::getReportName, nullsLast());
            case "createdAt" -> c = Comparator.comparing(Report::getCreatedAt, nullsLast());
            default -> c = Comparator.comparing(Report::getCreatedAt, nullsLast());
        }

        return asc ? c : c.reversed();
    }

    private static <T extends Comparable<? super T>> Comparator<T> nullsLast() {
        return Comparator.nullsLast(Comparator.naturalOrder());
    }


    @Override
    public Map<String, Object> summaryMetrics() {
        Map<String, Object> m = new LinkedHashMap<>();
        List<Report> all = repo.findAll();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime last7 = now.minusDays(7);

        long total = all.size();
        long last7Count = all.stream()
                .filter(r -> r.getCreatedAt() != null && !r.getCreatedAt().isBefore(last7))
                .count();
        long todayCount = all.stream()
                .filter(r -> r.getCreatedAt() != null && !r.getCreatedAt().isBefore(todayStart))
                .count();

        m.put("total", total);
        m.put("last7Days", last7Count);
        m.put("today", todayCount);
        return m;
    }

    @Override
    public List<Map<String, Object>> countByExam() {
         Map<Long, Long> grouped = repo.findAll().stream()
                .collect(Collectors.groupingBy(Report::getExamId, Collectors.counting()));
        return grouped.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .map(e -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("examId", e.getKey());
                    row.put("count", e.getValue());
                    return row;
                })
                .toList();
    }

    @Override
    public List<Map<String, Object>> countPerDaySince(int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);

        // In-memory filter and group by day
        Map<LocalDate, Long> grouped = repo.findAll().stream()
                .filter(r -> r.getCreatedAt() != null && !r.getCreatedAt().isBefore(since))
                .collect(Collectors.groupingBy(r -> r.getCreatedAt().toLocalDate(),
                        TreeMap::new, Collectors.counting()));

        return grouped.entrySet().stream()
                .map(e -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("day", e.getKey().toString()); // yyyy-MM-dd
                    row.put("count", e.getValue());
                    return row;
                })
                .toList();
    }


    private Report toEntity(ReportDTO d) {
        Report e = new Report();
        e.setId(d.getId());
        e.setExamId(d.getExamId());
        e.setStudentId(d.getStudentId());
        e.setReportName(d.getReportName());e.setReportDescription(d.getReportDescription());
        e.setCreatedAt(d.getCreatedAt());
      return e;
    }

    private ReportDTO toDto(Report e) {
        ReportDTO d = new ReportDTO();
        d.setId(e.getId());
        d.setExamId(e.getExamId());
        d.setStudentId(e.getStudentId());
        d.setReportName(e.getReportName());
        d.setReportDescription(e.getReportDescription());
        d.setCreatedAt(e.getCreatedAt());
        return d;
    }
}